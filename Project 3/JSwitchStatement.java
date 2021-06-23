package jminusminus;

import java.util.ArrayList;

class JSwitchStatement extends JStatement{

    private JExpression parExpression;
    ArrayList<JSwitchStatement> blockStatement;

    ArrayList<JStatement> blockStatements;
    ArrayList<JExpression> switchLabels;

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

    public JAST analyze(Context context) {
        return null;
    }

    public void codegen(CLEmitter output) {

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
                    if (blockStatement.get(i).blockStatement != null) {
                        for (int z = 0; z < blockStatement.get(i).blockStatement.size(); z++) {
                            blockStatement.get(i).blockStatement.get(z).toJSON(e2);
                        }
                    }
                }
            }
        }
    }
}
