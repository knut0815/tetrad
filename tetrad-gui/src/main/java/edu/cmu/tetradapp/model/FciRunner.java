///////////////////////////////////////////////////////////////////////////////
// For information as to what this class does, see the Javadoc, below.       //
// Copyright (C) 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006,       //
// 2007, 2008, 2009, 2010, 2014, 2015 by Peter Spirtes, Richard Scheines, Joseph   //
// Ramsey, and Clark Glymour.                                                //
//                                                                           //
// This program is free software; you can redistribute it and/or modify      //
// it under the terms of the GNU General Public License as published by      //
// the Free Software Foundation; either version 2 of the License, or         //
// (at your option) any later version.                                       //
//                                                                           //
// This program is distributed in the hope that it will be useful,           //
// but WITHOUT ANY WARRANTY; without even the implied warranty of            //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             //
// GNU General Public License for more details.                              //
//                                                                           //
// You should have received a copy of the GNU General Public License         //
// along with this program; if not, write to the Free Software               //
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA //
///////////////////////////////////////////////////////////////////////////////

package edu.cmu.tetradapp.model;

import edu.cmu.tetrad.data.IKnowledge;
import edu.cmu.tetrad.graph.*;
import edu.cmu.tetrad.search.*;
import edu.cmu.tetrad.util.Params;
import edu.cmu.tetrad.util.TetradSerializableUtils;
import edu.cmu.tetradapp.util.IonInput;

import java.util.ArrayList;
import java.util.List;


/**
 * Extends AbstractAlgorithmRunner to produce a wrapper for the FCI algorithm.
 *
 * @author Joseph Ramsey
 */
public class FciRunner extends AbstractAlgorithmRunner
        implements IndTestProducer, GraphSource, IonInput {
    static final long serialVersionUID = 23L;

    //=========================CONSTRUCTORS================================//

    public FciRunner(DataWrapper dataWrapper, Params params) {
        super(dataWrapper, params, null);
    }

    /**
     * Constucts a wrapper for the given EdgeListGraph.
     */
    public FciRunner(GraphSource graphWrapper, Params params, KnowledgeBoxModel knowledgeBoxModel) {
        super(graphWrapper.getGraph(), params, knowledgeBoxModel);
    }


    public FciRunner(DataWrapper dataWrapper, Params params, KnowledgeBoxModel knowledgeBoxModel) {
        super(dataWrapper, params, knowledgeBoxModel);
    }

    public FciRunner(Graph graph, Params params) {
        super(graph, params);
    }

    public FciRunner(Graph graph, Params params, KnowledgeBoxModel knowledgeBoxModel) {
        super(graph, params, knowledgeBoxModel);
    }

    public FciRunner(GraphWrapper graphWrapper, Params params) {
        super(graphWrapper.getGraph(), params);
    }

    public FciRunner(DagWrapper dagWrapper, Params params) {
        super(dagWrapper.getDag(), params);
    }

    public FciRunner(SemGraphWrapper dagWrapper, Params params) {
        super(dagWrapper.getGraph(), params);
    }

    public FciRunner(IndependenceFactsModel model, Params params) {
        super(model, params, null);
    }

    public FciRunner(IndependenceFactsModel model, Params params, KnowledgeBoxModel knowledgeBoxModel) {
        super(model, params, knowledgeBoxModel);
    }


    /**
     * Generates a simple exemplar of this class to test serialization.
     *
     * @see TetradSerializableUtils
     */
    public static FciRunner serializableInstance() {
        return new FciRunner(Dag.serializableInstance(), new Params());
    }

    //=================PUBLIC METHODS OVERRIDING ABSTRACT=================//

    /**
     * Executes the algorithm, producing (at least) a result workbench. Must be
     * implemented in the extending class.
     */
    public void execute() {
        IKnowledge knowledge = getParams().getKnowledge();

        Graph graph;

        if (getParams().isRFCI_Used()) {
            Rfci fci = new Rfci(getIndependenceTest());
            fci.setKnowledge(knowledge);
            fci.setCompleteRuleSetUsed(getParams().isCompleteRuleSetUsed());
            fci.setMaxPathLength(getParams().getMaxReachablePathLength());
            fci.setDepth(getParams().getDepth());
            graph = fci.search();
        } else {
            Fci fci = new Fci(getIndependenceTest());
            fci.setKnowledge(knowledge);
            fci.setCompleteRuleSetUsed(getParams().isCompleteRuleSetUsed());
            fci.setPossibleDsepSearchDone(getParams().isPossibleDsepDone());
            fci.setMaxPathLength(getParams().getMaxReachablePathLength());
            fci.setDepth(getParams().getDepth());
            graph = fci.search();
        }

        if (getSourceGraph() != null) {
            GraphUtils.arrangeBySourceGraph(graph, getSourceGraph());
        } else if (knowledge.isDefaultToKnowledgeLayout()) {
            SearchGraphUtils.arrangeByKnowledgeTiers(graph, knowledge);
        } else {
            GraphUtils.circleLayout(graph, 200, 200, 150);
        }

        setResultGraph(graph);
    }

    public IndependenceTest getIndependenceTest() {
        Object dataModel = getDataModel();

        if (dataModel == null) {
            dataModel = getSourceGraph();
        }

        Params params = getParams();
        IndTestType testType;

        if (getParams() instanceof Params) {
            Params _params = (Params) params;
            testType = _params.getIndTestType();
        } else {
            Params _params = (Params) params;
            testType = _params.getIndTestType();
        }

        return new IndTestChooser().getTest(dataModel, params, testType);
    }

    public Graph getGraph() {
        return getResultGraph();
    }


    /**
     * @return the names of the triple classifications. Coordinates with
     */
    public List<String> getTriplesClassificationTypes() {
        List<String> names = new ArrayList<String>();
//        names.add("Definite Colliders");
//        names.add("Definite Noncolliders");
        return names;
    }

    /**
     * @return the list of triples corresponding to <code>getTripleClassificationNames</code>.
     */
    public List<List<Triple>> getTriplesLists(Node node) {
        List<List<Triple>> triplesList = new ArrayList<List<Triple>>();
        Graph graph = getGraph();
//        triplesList.add(DataGraphUtils.getDefiniteCollidersFromGraph(node, graph));
//        triplesList.add(DataGraphUtils.getDefiniteNoncollidersFromGraph(node, graph));
        return triplesList;
    }

    public boolean supportsKnowledge() {
        return true;
    }

    @Override
    public String getAlgorithmName() {
        return "FCI";
    }
}


