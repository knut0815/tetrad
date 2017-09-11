package edu.cmu.tetrad.algcomparison.algorithm.oracle.pattern;

import edu.cmu.tetrad.algcomparison.algorithm.Algorithm;
import edu.cmu.tetrad.algcomparison.independence.IndependenceWrapper;
import edu.cmu.tetrad.algcomparison.utils.HasKnowledge;
import edu.cmu.tetrad.algcomparison.utils.TakesIndependenceWrapper;
import edu.cmu.tetrad.annotation.AlgType;
import edu.cmu.tetrad.data.DataModel;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.data.DataType;
import edu.cmu.tetrad.data.IKnowledge;
import edu.cmu.tetrad.data.Knowledge2;
import edu.cmu.tetrad.graph.EdgeListGraph;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.GraphUtils;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.search.IndependenceTest;
import edu.cmu.tetrad.util.Parameters;
import edu.pitt.dbmi.algo.bootstrap.BootstrapEdgeEnsemble;
import edu.pitt.dbmi.algo.bootstrap.GeneralBootstrapTest;
import java.util.List;

/**
 * PC.
 *
 * @author jdramsey
 */
@edu.cmu.tetrad.annotation.Algorithm(
        name = "MBFS",
        command = "mbfs",
        algoType = AlgType.search_for_Markov_blankets,
        description = "Short blurb goes here",
        assumptions = {}
)
public class MBFS implements Algorithm, HasKnowledge, TakesIndependenceWrapper {

    static final long serialVersionUID = 23L;
    private IndependenceWrapper test;
    private IKnowledge knowledge = new Knowledge2();
    private String targetName;

    public MBFS() {
    }

    public MBFS(IndependenceWrapper type) {
        this.test = type;
    }

    @Override
    public Graph search(DataModel dataSet, Parameters parameters) {
        if (!parameters.getBoolean("bootstrapping")) {
            IndependenceTest test = this.test.getTest(dataSet, parameters);
            edu.cmu.tetrad.search.Mbfs search = new edu.cmu.tetrad.search.Mbfs(
                    test,
                    parameters.getInt("depth")
            );

            search.setDepth(parameters.getInt("depth"));
            search.setKnowledge(knowledge);

            this.targetName = parameters.getString("targetName");
            Node target = test.getVariable(targetName);
            return search.search(target);
        } else {
            MBFS algorithm = new MBFS(test);

            algorithm.setKnowledge(knowledge);
            DataSet data = (DataSet) dataSet;
            GeneralBootstrapTest search = new GeneralBootstrapTest(data, algorithm,
                    parameters.getInt("bootstrapSampleSize"));

            BootstrapEdgeEnsemble edgeEnsemble = BootstrapEdgeEnsemble.Highest;
            switch (parameters.getInt("bootstrapEnsemble", 1)) {
                case 0:
                    edgeEnsemble = BootstrapEdgeEnsemble.Preserved;
                    break;
                case 1:
                    edgeEnsemble = BootstrapEdgeEnsemble.Highest;
                    break;
                case 2:
                    edgeEnsemble = BootstrapEdgeEnsemble.Majority;
            }
            search.setEdgeEnsemble(edgeEnsemble);
            search.setParameters(parameters);
            search.setVerbose(parameters.getBoolean("verbose"));
            return search.search();
        }
    }

    @Override
    public Graph getComparisonGraph(Graph graph) {
        Node target = graph.getNode(targetName);
        return GraphUtils.markovBlanketDag(target, new EdgeListGraph(graph));
    }

    @Override
    public String getDescription() {
        return "MBFS (Markov Blanket Fan Search) using " + test.getDescription();
    }

    @Override
    public DataType getDataType() {
        return test.getDataType();
    }

    @Override
    public List<String> getParameters() {
        List<String> parameters = test.getParameters();
        parameters.add("depth");
        parameters.add("targetName");
        // Bootstrapping
        parameters.add("bootstrapping");
        parameters.add("bootstrapSampleSize");
        parameters.add("bootstrapEnsemble");
        parameters.add("verbose");
        return parameters;
    }

    @Override
    public IKnowledge getKnowledge() {
        return knowledge;
    }

    @Override
    public void setKnowledge(IKnowledge knowledge) {
        this.knowledge = knowledge;
    }

    @Override
    public void setIndependenceWrapper(IndependenceWrapper test) {
        this.test = test;
    }
}
