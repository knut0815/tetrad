package edu.cmu.tetrad.algcomparison.algorithm.multi;

import edu.cmu.tetrad.algcomparison.algorithm.MultiDataSetAlgorithm;
import edu.cmu.tetrad.algcomparison.algorithm.oracle.pattern.Fges;
import edu.cmu.tetrad.algcomparison.score.BdeuScore;
import edu.cmu.tetrad.algcomparison.utils.HasKnowledge;
import edu.cmu.tetrad.annotation.AlgType;
import edu.cmu.tetrad.data.*;
import edu.cmu.tetrad.graph.EdgeListGraph;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.search.BdeuScoreImages;
import edu.cmu.tetrad.search.SearchGraphUtils;
import edu.cmu.tetrad.util.Parameters;
import edu.pitt.dbmi.algo.bootstrap.BootstrapEdgeEnsemble;
import edu.pitt.dbmi.algo.bootstrap.GeneralBootstrapTest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wraps the IMaGES algorithm for discrete variables.
 * </p>
 * Requires that the parameter 'randomSelectionSize' be set to indicate how many
 * datasets should be taken at a time (randomly). This cannot given multiple
 * values.
 *
 * @author jdramsey
 */
@edu.cmu.tetrad.annotation.Algorithm(
        name = "IMaGES Discrete",
        command = "imgs_disc",
        algoType = AlgType.forbid_latent_common_causes,
        description = "Adjusts the discrete BDeu variable score of FGES so allow for multiple datasets as input. The BDeu scores for each data set are averaged at each step of the algorithm, producing a model for al data sets that assumes they have the same graphical structure across dataset. Note that in order to use this algorithm in a nontrivial way, one needs to have loaded or simulated multiple dataset.\n" +
                "\n" +
                "Input Assumptions: A set of discrete datasets with the same variables and sample sizes. \n" +
                "\n" +
                "Output Format: A pattern, interpreted as a common model for all datasets. \n" +
                "\n" +
                "Parameters: All of the parameters from FGES are available for IMaGES. Additionally:\n" +
                "- The number of runs. The number of times the algorithm should select data sets and 90 run the algorithm. Default 1.\n" +
                "- The number of datasets that should be taken in each random sample. IMaGES will randomly select a set of datasets to run, so that on different runs one can be an estimate of the consistency of results. To use all variables, set this to the total number of datasets. Default 1.\n"
)
public class ImagesBDeu implements MultiDataSetAlgorithm, HasKnowledge {

    static final long serialVersionUID = 23L;
    private IKnowledge knowledge = new Knowledge2();

    public ImagesBDeu() {
    }

    @Override
    public Graph search(List<DataModel> dataSets, Parameters parameters) {
    	if (parameters.getInt("bootstrapSampleSize") < 1) {
            BdeuScoreImages score = new BdeuScoreImages(dataSets);
            score.setSamplePrior(parameters.getDouble("samplePrior"));
            score.setStructurePrior(parameters.getDouble("structurePrior"));
            edu.cmu.tetrad.search.Fges search = new edu.cmu.tetrad.search.Fges(score);
            search.setFaithfulnessAssumed(true);
            search.setKnowledge(knowledge);

            return search.search();
        } else {
            ImagesBDeu imagesBDeu = new ImagesBDeu();
            //imagesBDeu.setKnowledge(knowledge);

            List<DataSet> datasets = new ArrayList<>();

            for (DataModel dataModel : dataSets) {
                datasets.add((DataSet) dataModel);
            }
            GeneralBootstrapTest search = new GeneralBootstrapTest(datasets, imagesBDeu,
                    parameters.getInt("bootstrapSampleSize"));
            search.setKnowledge(knowledge);

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
    public Graph search(DataModel dataSet, Parameters parameters) {
        if (!parameters.getBoolean("bootstrapping")) {
            return search(Collections.singletonList((DataModel) DataUtils.getDiscreteDataSet(dataSet)), parameters);
        } else {
            ImagesBDeu imagesBDeu = new ImagesBDeu();
            imagesBDeu.setKnowledge(knowledge);

            List<DataSet> dataSets = Collections.singletonList(DataUtils.getContinuousDataSet(dataSet));
            GeneralBootstrapTest search = new GeneralBootstrapTest(dataSets, imagesBDeu,
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
        return SearchGraphUtils.patternForDag(new EdgeListGraph(graph));
    }

    @Override
    public String getDescription() {
        return "IMaGES for discrete variables (using the BDeu score)";
    }

    @Override
    public DataType getDataType() {
        return DataType.Discrete;
    }

    @Override
    public List<String> getParameters() {
        List<String> parameters = new Fges(new BdeuScore(), false).getParameters();
        parameters.add("numRuns");
        parameters.add("randomSelectionSize");
        // Bootstrapping
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
}
