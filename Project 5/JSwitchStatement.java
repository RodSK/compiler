package jminusminus;

import java.util.ArrayList;

import static jminusminus.CLConstants.*;

class JSwitchStatement extends JStatement{

    private JExpression parExpression;
    ArrayList<JSwitchStatement> blockStatement;

    ArrayList<JStatement> blockStatements;
    ArrayList<JExpression> switchLabels;

    // Label Out of Switch Case
    private String out;

    boolean group;

    public JSwitchStatement(int line, JExpression parExpression,
                            ArrayList<JSwitchStatement> blockStatement,
                            ArrayList<JExpression> switchLabels, ArrayList<JStatement> blockStatements, boolean group ){
        super(line);
        this.parExpression = parExpression;
        this.blockStatement = blockStatement;
        this.blockStatements = blockStatements;
        this.switchLabels = switchLabels;
        this.group = group;
    }

    // get Out Label
    public String getBreakLabel(){
        return out;
    }

    public JSwitchStatement analyze(Context context) {
        JMember.breakStack.push(this);
        JExpression tmp;
        JStatement st;
        parExpression = (JExpression) parExpression.analyze(context);
        parExpression.type().mustMatchExpected(line(), Type.INT);
        if (blockStatement != null) {
            for (int i = 0; i < blockStatement.size(); i++) {
               if (blockStatement.get(i).switchLabels.get(0) != null) {
                   tmp = blockStatement.get(i).switchLabels.get(0);
                   tmp = (JExpression) tmp.analyze(context);
                   tmp.type().mustMatchExpected(line(), Type.INT);
                   blockStatement.get(i).switchLabels.set(0, tmp);
               }
               if(!blockStatement.get(i).blockStatements.isEmpty()) {
                   for (int z = 0; z < blockStatement.get(i).blockStatements.size(); z++) {
                       st = blockStatement.get(i).blockStatements.get(z);
                       st = (JStatement) st.analyze(context);
                       blockStatement.get(i).blockStatements.set(z, st);
                   }
               }
            }
        }

        JMember.breakStack.pop();
        return this;
    }

    public void codegen(CLEmitter output) {
        ArrayList<String> caseLabel = new ArrayList<String>();
        for (int i = 0; i < blockStatement.size(); i++) {
            caseLabel.add(output.createLabel());
        }
        out = output.createLabel();

        for (int i = 0; i < blockStatement.size(); i++) {
            output.addLabel(caseLabel.get(i));
            if (blockStatement.get(i).switchLabels.get(0) != null) {
                blockStatement.get(i).switchLabels.get(0).codegen(output);
                parExpression.codegen(output);

                if (i == blockStatement.size() - 1) {
                    output.addBranchInstruction(IF_ICMPNE, out);
                } else {
                    output.addBranchInstruction(IF_ICMPNE, caseLabel.get(i + 1));
                }
            }
            if (!blockStatement.get(i).blockStatements.isEmpty()) {
                for (int z = 0; z < blockStatement.get(i).blockStatements.size(); z++) {
                    blockStatement.get(i).blockStatements.get(z).codegen(output);
                }
            }

        }

        output.addLabel(out);
    }

    public void toJSON(JSONElement json) {
        if(!group) {
            JSONElement e = new JSONElement();
            json.addChild("JSwitchStatement:" + line, e);

            JSONElement e1 = new JSONElement();
            e.addChild("Condition", e1);
            parExpression.toJSON(e1);

            if (blockStatement != null) {
                for (int i = 0; i < blockStatement.size(); i++) {
                    JSONElement e2 = new JSONElement();
                    e.addChild("SwitchStatementGroup", e2);

                    JSONElement e3 = new JSONElement();
                    if (blockStatement.get(i).switchLabels.get(0) != null) {
                        e2.addChild("Case", e3);
                        blockStatement.get(i).switchLabels.get(0).toJSON(e3);
                    } else {
                        e2.addChild("Default", e3);
                    }
                    if (blockStatement.get(i).blockStatements != null) {
                        for (int z = 0; z < blockStatement.get(i).blockStatements.size(); z++) {
                            blockStatement.get(i).blockStatements.get(z).toJSON(e2);
                        }
                    }
                }
            }
        }
    }
}
