package jminusminus;

import java.util.ArrayList;

import static jminusminus.CLConstants.*;

/**
 * The AST node for a For-Statement.
 */
class JForStatement extends JStatement {

    ArrayList<JStatement> forInit;
    JExpression expr;
    ArrayList<JStatement> forUpdate;
    JStatement statement;
    private String continueLabel;

    /**
     * Constructs an AST node for an for-statement.
     *
     * @param line       line in which the for-statement occurs in the source file.
     * @param forInit    Init
     * @param expr       Condition
     * @param forUpdate  Update
     * @param statement  Body
     */
    public JForStatement(int line, ArrayList<JStatement> forInit, JExpression expr, ArrayList<JStatement> forUpdate, JStatement statement) {
        super(line);
        this.forInit = forInit;
        this.expr = expr;
        this.forUpdate = forUpdate;
        this.statement = statement;
    }

    // Get Top label of Loop
    public String getContinueLabel(){
        return continueLabel;
    }

    /**
     * {@inheritDoc}
     */
    public JStatement analyze(Context context) {
        JMember.continueStack.push(this);

        JStatement temp;
        if (forInit != null) {
            for (int i = 0; i < forInit.size(); i++) {
                temp = (JVariableDeclaration) forInit.get(i).analyze(context);
                forInit.set(i, temp);
            }
        }
        expr = expr.analyze(context);
        expr.type().mustMatchExpected(line(), Type.BOOLEAN);
        if (forUpdate != null) {
            for (int i = 0; i < forUpdate.size(); i++) {
                temp = (JStatement) forUpdate.get(i).analyze(context);
                forUpdate.set(i, temp);
            }
        }
        statement = (JStatement) statement.analyze(context);
        JMember.continueStack.pop();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        if (forInit != null) {
            for (int i = 0; i < forInit.size(); i++) {
                forInit.get(i).codegen(output);
            }
        }

        continueLabel = output.createLabel();
        String forLabel = output.createLabel();
        String out = output.createLabel();
        continueLabel = output.createLabel();
        output.addLabel(forLabel);
        expr.codegen(output, out, false);
        statement.codegen(output);
        output.addLabel(continueLabel);

        if (forUpdate != null) {
            for (int i = 0; i < forUpdate.size(); i++) {
                forUpdate.get(i).codegen(output);
            }
        }

        output.addBranchInstruction(GOTO, forLabel);
        output.addLabel(out);
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JForStatement:" + line, e);

        JSONElement e1 = new JSONElement();
        e.addChild("Init", e1);
        forInit.get(0).toJSON(e1);

        JSONElement e2 = new JSONElement();
        e.addChild("Condition", e2);
        expr.toJSON(e2);

        JSONElement e3 = new JSONElement();
        e.addChild("Update", e3);
        forUpdate.get(0).toJSON(e3);

        JSONElement e4 = new JSONElement();
        e.addChild("Body", e4);
        statement.toJSON(e4);

//        if (elsePart != null) {
//            JSONElement e3 = new JSONElement();
//            e.addChild("ElsePart", e3);
//            elsePart.toJSON(e3);
//        }
    }
}
