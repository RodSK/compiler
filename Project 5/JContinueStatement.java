package jminusminus;

import static jminusminus.CLConstants.GOTO;

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

    private Object obj;
    /**
     * {@inheritDoc}
     */
    public JStatement analyze(Context context) {
        if (JMember.continueStack.isEmpty()){
            JAST.compilationUnit.reportSemanticError(line,
                    "Incorrect use of Continue Statement.");
        }else {
            obj = JMember.continueStack.peek();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        if (obj instanceof JForStatement) {
            JForStatement classObj = (JForStatement) obj;
            output.addBranchInstruction(GOTO, classObj.getContinueLabel());
        } else if (obj instanceof JDoStatement) {
            JDoStatement classObj = (JDoStatement) obj;
            output.addBranchInstruction(GOTO, classObj.getContinueLabel());
        } else if (obj instanceof JWhileStatement) {
            JWhileStatement classObj = (JWhileStatement) obj;
            output.addBranchInstruction(GOTO, classObj.getContinueLabel());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JContinueStatement:" + line, e);

    }
}


