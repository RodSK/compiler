// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * The AST node for a while-statement.
 */
class JWhileStatement extends JStatement {
    // Test expression.
    private JExpression condition;

    // The body.
    private JStatement body;

    // Label Out of Loop
    private String out;
    // Label Continue
    private String test;

    /**
     * Constructs an AST node for a while-statement.
     *
     * @param line      line in which the while-statement occurs in the source file.
     * @param condition test expression.
     * @param body      the body.
     */
    public JWhileStatement(int line, JExpression condition, JStatement body) {
        super(line);
        this.condition = condition;
        this.body = body;
    }

    // get Out Label
    public String getBreakLabel(){
        return out;
    }

    // Get Top label of Loop
    public String getContinueLabel(){
        return test;
    }

    /**
     * {@inheritDoc}
     */
    public JWhileStatement analyze(Context context) {
        JMember.breakStack.push(this);
        JMember.continueStack.push(this);
        condition = condition.analyze(context);
        condition.type().mustMatchExpected(line(), Type.BOOLEAN);
        body = (JStatement) body.analyze(context);
        JMember.breakStack.pop();
        JMember.continueStack.pop();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        test = output.createLabel();
        out = output.createLabel();
        output.addLabel(test);
        condition.codegen(output, out, false);
        body.codegen(output);
        output.addBranchInstruction(GOTO, test);
        output.addLabel(out);
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JWhileStatement:" + line, e);
        JSONElement e1 = new JSONElement();
        e.addChild("Condition", e1);
        condition.toJSON(e1);
        JSONElement e2 = new JSONElement();
        e.addChild("Body", e2);
        body.toJSON(e2);
    }

}
