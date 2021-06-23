package jminusminus;

import java.util.ArrayList;

class JSwitchStatementGroup extends JStatement{

    ArrayList<JExpression> switchLabels;
    ArrayList<JStatement> blockStatement;

    public JSwitchStatementGroup(int line, ArrayList<JExpression> switchLabels,
                                 ArrayList<JStatement> blockStatement) {
        super(line);
        this.switchLabels = switchLabels;
        this.blockStatement = blockStatement;
    }

    public JAST analyze(Context context) {
        return null;
    }

    public void codegen(CLEmitter output) {

    }

}
