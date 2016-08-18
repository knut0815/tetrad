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
import edu.cmu.tetrad.data.Knowledge2;
import edu.cmu.tetrad.data.KnowledgeBoxInput;
import edu.cmu.tetrad.graph.EdgeListGraph;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.session.ParamsResettable;
import edu.cmu.tetrad.session.SessionModel;
import edu.cmu.tetrad.util.Parameters;
import edu.cmu.tetrad.util.TetradLogger;
import edu.cmu.tetrad.util.TetradSerializableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author kaalpurush
 */
public class KnowledgeBoxModel implements SessionModel, ParamsResettable, KnowledgeEditable,
        KnowledgeBoxInput, KnowledgeBoxNotifiable {
    static final long serialVersionUID = 23L;

    private String name;
    private Parameters params;
    private KnowledgeBoxInput knowledgeBoxInput;
    private List<String> varNames = new ArrayList<>();
    private List<Node> variables = new ArrayList<>();
    private List<String> variableNames = new ArrayList<>();
    private final Graph sourceGraph = new EdgeListGraph();

    public KnowledgeBoxModel(BayesPmWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(GraphWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(StandardizedSemImWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(SemImWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(SemPmWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(DataWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(TimeLagGraphWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(GeneralizedSemImWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(BayesImWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(SemGraphWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(GeneralizedSemPmWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(DagWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(DirichletBayesImWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(BuildPureClustersRunner wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(PurifyRunner wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(LofsRunner wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(MeasurementModelWrapper wrapper, Parameters params) {
        this((KnowledgeBoxInput) wrapper, params);
    }

    public KnowledgeBoxModel(KnowledgeBoxInput input, Parameters params) {
        this(params, input);
    }

    public KnowledgeBoxModel(KnowledgeBoxInput input1, KnowledgeBoxInput input2, Parameters params) {
        this(params, input1, input2);
    }

    public KnowledgeBoxModel(KnowledgeBoxInput input1, KnowledgeBoxInput input2,
                             KnowledgeBoxInput input3, Parameters params) {
        this(params, input1, input2, input3);
    }

    public KnowledgeBoxModel(KnowledgeBoxInput input1, KnowledgeBoxInput input2,
                             KnowledgeBoxInput input3, KnowledgeBoxInput input4, Parameters params) {
        this(params, input1, input2, input3, input4);
    }

    public KnowledgeBoxModel(KnowledgeBoxInput input1, KnowledgeBoxInput input2,
                             KnowledgeBoxInput input3, KnowledgeBoxInput input4,
                             KnowledgeBoxInput input5, Parameters params) {
        this(params, input1, input2, input3, input4, input5);
    }

    public KnowledgeBoxModel(KnowledgeBoxInput input1, KnowledgeBoxInput input2,
                             KnowledgeBoxInput input3, KnowledgeBoxInput input4,
                             KnowledgeBoxInput input5, KnowledgeBoxInput input6, Parameters params) {
        this(params, input1, input2, input3, input4, input5, input6);
    }

    public KnowledgeBoxModel(KnowledgeBoxInput input1, KnowledgeBoxInput input2,
                             KnowledgeBoxInput input3, KnowledgeBoxInput input4,
                             KnowledgeBoxInput input5, KnowledgeBoxInput input6,
                             KnowledgeBoxInput input7, Parameters params) {
        this(params, input1, input2, input3, input4, input5, input6, input7);
    }

    public KnowledgeBoxModel(KnowledgeBoxInput input1, KnowledgeBoxInput input2,
                             KnowledgeBoxInput input3, KnowledgeBoxInput input4,
                             KnowledgeBoxInput input5, KnowledgeBoxInput input6,
                             KnowledgeBoxInput input7, KnowledgeBoxInput input8, Parameters params) {
        this(params, input1, input2, input3, input4, input5, input6, input7, input8);
    }

    public KnowledgeBoxModel(KnowledgeBoxInput input1, KnowledgeBoxInput input2,
                             KnowledgeBoxInput input3, KnowledgeBoxInput input4,
                             KnowledgeBoxInput input5, KnowledgeBoxInput input6,
                             KnowledgeBoxInput input7, KnowledgeBoxInput input8,
                             KnowledgeBoxInput input9, Parameters params) {
        this(params, input1, input2, input3, input4, input5, input6, input7, input8, input9);
    }

    public KnowledgeBoxModel(KnowledgeBoxInput input1, KnowledgeBoxInput input2,
                             KnowledgeBoxInput input3, KnowledgeBoxInput input4,
                             KnowledgeBoxInput input5, KnowledgeBoxInput input6,
                             KnowledgeBoxInput input7, KnowledgeBoxInput input8,
                             KnowledgeBoxInput input9, KnowledgeBoxInput input10, Parameters params) {
        this(params, input1, input2, input3, input4, input5, input6, input7, input8, input9, input10);
    }

    /**
     * Constructor from dataWrapper edge
     */
    public KnowledgeBoxModel(Parameters params, KnowledgeBoxInput...inputs) {
        if (params == null) {
            throw new NullPointerException();
        }

        for (KnowledgeBoxInput input : inputs) {
            if (input == null) {
                throw new NullPointerException();
            }
        }

        SortedSet<Node> variableNodes = new TreeSet<>();
        SortedSet<String> variableNames = new TreeSet<>();

        for (KnowledgeBoxInput input : inputs) {
            variableNodes.addAll(input.getVariables());
            variableNames.addAll(input.getVariableNames());
        }

        this.variables = new ArrayList<>(variableNodes);
        this.variableNames = new ArrayList<>(variableNames);

        this.params = params;
        this.setKnowledgeBoxInput(this);

//        if (this.knowledgeBoxInput == null) {
//            /**
//             * @serial
//             * @deprecated
//             */
//            this.knowledgeBoxInput = new Knowledge2BoxInput() {
//                static final long serialVersionUID = 23L;
//                public Graph getSourceGraph() {return new EdgeListGraph();}
//                public List<Node> getVariables() {return new ArrayList<Node>();}
//                public List<String> getVariableNames() {return new ArrayList<String>();}
//                public void setName(String name) {}
//                public String getNode() {return "";}
//            };
//        }

        if (((IKnowledge) params.get("knowledge", new Knowledge2())).isEmpty()) {
            freshenKnowledgeIfEmpty(params);
        }

        TetradLogger.getInstance().log("info", "Knowledge");

        // This is a conundrum. At this point I dont know whether I am in a
        // simulation or not. If in a simulation, I should print the knowledge.
        // If not, I should wait for resetParams to be called. For now I'm
        // printing the knowledge if it's not empty.
        if (!((IKnowledge) params.get("knowledge", new Knowledge2())).isEmpty()) {
            TetradLogger.getInstance().log("knowledge", params.get("knowledge", new Knowledge2()).toString());
        }
    }

    private void freshenKnowledgeIfEmpty(Parameters params) {
        if (((IKnowledge) params.get("knowledge", new Knowledge2())).isEmpty()) {
            createKnowledge((IKnowledge) params.get("knowledge", new Knowledge2()));
            varNames = new ArrayList<>();

            for (String varName : getKnowledgeBoxInput().getVariableNames()) {
                if (!varName.startsWith("E_")) {
                    varNames.add(varName);
                }
            }
        }
    }

    /**
     * Generates a simple exemplar of this class to test serialization.
     *
     * @see TetradSerializableUtils
     */
    public static KnowledgeBoxModel serializableInstance() {
        return new KnowledgeBoxModel(new Parameters(), GraphWrapper.serializableInstance());
    }

    private IKnowledge createKnowledge(IKnowledge knowledge) {
        knowledge.clear();
        for (String varName : varNames) {
            knowledge.addVariable(varName);
        }
        return knowledge;
    }

    public IKnowledge getKnowledge() {
        return (IKnowledge) params.get("knowledge", new Knowledge2());
    }

    public String getName() {
        return this.name;
    }

    public Graph getSourceGraph() {
        return sourceGraph;
    }

    public Graph getResultGraph() {
        return sourceGraph;
    }

    public List<String> getVarNames() {
        return varNames;
    }

    public void setKnowledge(IKnowledge knowledge) {
        params.set("knowledge", knowledge);
        TetradLogger.getInstance().log("knowledge", knowledge.toString());
    }

    public void setName(String name) {
        this.name = name;
	}

    public void resetParams(Object params) {
        this.params = (Parameters) params;
        freshenKnowledgeIfEmpty(this.params);
        TetradLogger.getInstance().log("knowledge", this.params.get("knowledge", new Knowledge2()).toString());
    }

    public Object getResettableParams() {
        return this.params;
    }

    public List<Node> getVariables() {
        return variables;
    }

    public List<String> getVariableNames() {
        return variableNames;
    }

    KnowledgeBoxInput getKnowledgeBoxInput() {
        if (knowledgeBoxInput == null) {
            return this;
        }
        else {
            return knowledgeBoxInput;
        }
    }

    void setKnowledgeBoxInput(KnowledgeBoxInput knowledgeBoxInput) {
        this.knowledgeBoxInput = knowledgeBoxInput;
    }
}



