package jminusminus;

/**
 * The AST node for an Continue-Statement.
 */
class JContinueStatement extends JStatement {

    /**
     * Constructs an AST node for an continue-statement.
     *
     * @param line      line in which the continue-statement occurs in the source file.
     */
    public JContinueStatement(int line) {
        super(line);
    }

    /**
     * {@inheritDoc}
     */
    public JStatement analyze(Context context) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {

    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JContinueStatement:" + line, e);

    }
}


