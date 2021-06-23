package jminusminus;

/**
 * The AST node for a DO-Statement.
 */
class JDoStatement extends JStatement {

    private JStatement body;
    private JExpression condition;

    // Label Out of Loop
    private String out;
    // Label Continue
    private String continueLabel;

    /**
     * Constructs an AST node for a while-statement.
     *
     * @param line      line in which the while-statement occurs in the source file.
     * @param body      the body.
     * @param condition test expression.
     */
    public JDoStatement(int line, JStatement body, JExpression condition) {
        super(line);
        this.body = body;
        this.condition = condition;
    }

    // get Out Label
    public String getBreakLabel(){
        return out;
    }

    // Get Top label of Loop
    public String getContinueLabel(){
        return continueLabel;
    }

    /**
     * {@inheritDoc}
     */
    public JDoStatement analyze(Context context) {
        JMember.breakStack.push(this);
        JMember.continueStack.push(this);
        condition = condition.analyze(context);
        condition.type().mustMatchExpected(line(), Type.BOOLEAN);
        body = (JStatement) body.analyze(context);
        JMember.continueStack.pop();
        JMember.breakStack.pop();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        String doLabel = output.createLabel();
        out = output.createLabel();
        continueLabel = output.createLabel();
        output.addLabel(doLabel);
        body.codegen(output);
        output.addLabel(continueLabel);
        condition.codegen(output, doLabel, true);
        output.addLabel(out);
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JDoStatement:" + line, e);
        JSONElement e1 = new JSONElement();
        e.addChild("Body", e1);
        body.toJSON(e1);
        JSONElement e2 = new JSONElement();
        e.addChild("Condition", e2);
        condition.toJSON(e2);
    }
}

