// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * This abstract base class is the AST node for an assignment operation.
 */
abstract class JAssignment extends JBinaryExpression {
    /**
     * Constructs an AST node for an assignment operation.
     *
     * @param line     line in which the assignment operation occurs in the source file.
     * @param operator the assignment operator.
     * @param lhs      the lhs operand.
     * @param rhs      the rhs operand.
     */
    public JAssignment(int line, String operator, JExpression lhs, JExpression rhs) {
        super(line, operator, lhs, rhs);
    }
}

/**
 * The AST node for an assignment (=) operation.
 */
class JAssignOp extends JAssignment {
    /**
     * Constructs the AST node for an assignment (=) operation..
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  lhs operand.
     * @param rhs  rhs operand.
     */
    public JAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "=", lhs, rhs);
    }

    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        rhs.type().mustMatchExpected(line(), lhs.type());
        type = rhs.type();
        if (lhs instanceof JVariable) {
            IDefn defn = ((JVariable) lhs).iDefn();
            if (defn != null) {
                // Local variable; consider it to be initialized now.
                ((LocalVariableDefn) defn).initialize();
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsLvalue(output);
        rhs.codegen(output);
        if (!isStatementExpression) {
            ((JLhs) lhs).codegenDuplicateRvalue(output);
        }
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a plus-assign (+=) operation.
 */
class JPlusAssignOp extends JAssignment {
    /**
     * Constructs the AST node for a plus-assign (+=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JPlusAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "+=", lhs, rhs);
    }

    // addNoArgInstruction
    int inst;

    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type().equals(Type.INT)) {
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
            inst = IADD;
        } else if (lhs.type().equals(Type.STRING)) {
            rhs = (new JStringConcatenationOp(line, lhs, rhs)).analyze(context);
            type = Type.STRING;
        } else if (lhs.type().equals(Type.LONG)) {
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
            inst = LADD;
        } else if (lhs.type().equals(Type.DOUBLE)) {
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
            inst = DADD;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for +=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsLvalue(output);
        if (lhs.type().equals(Type.STRING)) {
            rhs.codegen(output);
        } else {
            ((JLhs) lhs).codegenLoadLhsRvalue(output);
            rhs.codegen(output);
            output.addNoArgInstruction(inst);
        }
        if (!isStatementExpression) {
            ((JLhs) lhs).codegenDuplicateRvalue(output);
        }
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a Minus-Assign (-=) operation.
 */
class JMinusAssignOp extends JAssignment {
    /**
     * Constructs the AST node for a minus-assign (-=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JMinusAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "-=", lhs, rhs);
    }

    int inst;
    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type().equals(Type.INT)) {
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
            inst = ISUB;
        } else if (lhs.type().equals(Type.LONG)) {
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
            inst = LSUB;
        } else if (lhs.type().equals(Type.DOUBLE)) {
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
            inst = DSUB;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for -=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsRvalue(output);
        rhs.codegen(output);
        output.addNoArgInstruction(inst);
        if (!isStatementExpression) {
            ((JLhs) lhs).codegenDuplicateRvalue(output);
        }
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a Star-Assign (*=) operation.
 */
class JStarAssignOp extends JAssignment {
    /**
     * Constructs the AST node for a star-assign (*=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JStarAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "*=", lhs, rhs);
    }

    int inst;
    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type().equals(Type.INT)) {
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
            inst = IMUL;
        } else if (lhs.type().equals(Type.LONG)) {
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
            inst = LMUL;
        } else if (lhs.type().equals(Type.DOUBLE)) {
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
            inst = DMUL;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for *=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsRvalue(output);
        rhs.codegen(output);
        output.addNoArgInstruction(inst);
        if (!isStatementExpression) {
            ((JLhs) lhs).codegenDuplicateRvalue(output);
        }
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a Div-Assign (/=) operation.
 */
class JDivAssignOp extends JAssignment {
    /**
     * Constructs the AST node for a div-assign (/=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JDivAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "/=", lhs, rhs);
    }

    int inst;
    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type().equals(Type.INT)) {
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
            inst = IDIV;
        } else if (lhs.type().equals(Type.LONG)) {
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
            inst = LDIV;
        } else if (lhs.type().equals(Type.DOUBLE)) {
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
            inst = DDIV;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for /=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsRvalue(output);
        rhs.codegen(output);
        output.addNoArgInstruction(inst);
        ((JLhs) lhs).codegenDuplicateRvalue(output);
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a Rem-Assign (%=) operation.
 */
class JRemAssignOp extends JAssignment {
    /**
     * Constructs the AST node for a rem-assign (%=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JRemAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "%=", lhs, rhs);
    }

    int inst;
    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type().equals(Type.INT)) {
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
            inst = IREM;
        } else if (lhs.type().equals(Type.LONG)) {
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
            inst = LREM;
        } else if (lhs.type().equals(Type.DOUBLE)) {
            rhs.type().mustMatchExpected(line(), Type.DOUBLE);
            type = Type.DOUBLE;
            inst = DREM;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for %=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsRvalue(output);
        rhs.codegen(output);
        output.addNoArgInstruction(inst);
        ((JLhs) lhs).codegenDuplicateRvalue(output);
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a Or-Assign (|=) operation.
 */
class JOrAssignOp extends JAssignment {
    /**
     * Constructs the AST node for a or-assign (|=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JOrAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "|=", lhs, rhs);
    }

    int inst;
    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type().equals(Type.INT)) {
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
            inst = IOR;
        } else if (lhs.type().equals(Type.LONG)) {
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
            inst = LOR;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for |=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsRvalue(output);
        rhs.codegen(output);
        output.addNoArgInstruction(inst);
        ((JLhs) lhs).codegenDuplicateRvalue(output);
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a And-Assign (&=) operation.
 */
class JAndAssignOp extends JAssignment {
    /**
     * Constructs the AST node for an and-assign (&=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JAndAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "&=", lhs, rhs);
    }

    int inst;
    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type().equals(Type.INT)) {
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
            inst = IAND;
        } else if (lhs.type().equals(Type.LONG)) {
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
            inst = LAND;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for &=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsRvalue(output);
        rhs.codegen(output);
        output.addNoArgInstruction(inst);
        ((JLhs) lhs).codegenDuplicateRvalue(output);
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a Xor-Assign (^=) operation.
 */
class JXorAssignOp extends JAssignment {
    /**
     * Constructs the AST node for a xor-assign (^=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JXorAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "^=", lhs, rhs);
    }

    int inst;
    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type().equals(Type.INT)) {
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
            inst = IXOR;
        } else if (lhs.type().equals(Type.LONG)) {
            rhs.type().mustMatchExpected(line(), Type.LONG);
            type = Type.LONG;
            inst = LXOR;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for ^=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsRvalue(output);
        rhs.codegen(output);
        output.addNoArgInstruction(inst);
        ((JLhs) lhs).codegenDuplicateRvalue(output);
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a ALeftShift-Assign (<<=) operation.
 */
class JALeftShiftAssignOp extends JAssignment {
    /**
     * Constructs the AST node for a ALeftShift-assign (<<=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JALeftShiftAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "<<=", lhs, rhs);
    }

    int inst;
    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        rhs.type().mustMatchExpected(line(), Type.INT);
        if (lhs.type().equals(Type.INT)) {
            type = Type.INT;
            inst = ISHL;
        } else if (lhs.type().equals(Type.LONG)) {
            type = Type.LONG;
            inst = LSHL;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for <<=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsRvalue(output);
        rhs.codegen(output);
        output.addNoArgInstruction(inst);
        ((JLhs) lhs).codegenDuplicateRvalue(output);
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a ARightShift-Assign (>>=) operation.
 */
class JARightShiftAssignOp extends JAssignment {
    /**
     * Constructs the AST node for a ARightShift-assign (>>=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JARightShiftAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, ">>=", lhs, rhs);
    }

    int inst;
    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        rhs.type().mustMatchExpected(line(), Type.INT);
        if (lhs.type().equals(Type.INT)) {
            type = Type.INT;
            inst = ISHR;
        } else if (lhs.type().equals(Type.LONG)) {
            type = Type.LONG;
            inst = LSHR;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for >>=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsRvalue(output);
        rhs.codegen(output);
        output.addNoArgInstruction(inst);
        ((JLhs) lhs).codegenDuplicateRvalue(output);
        ((JLhs) lhs).codegenStore(output);
    }
}

/**
 * The AST node for a LRightShift-Assign (>>>=) operation.
 */
class JLRightShiftAssignOp extends JAssignment {
    /**
     * Constructs the AST node for a LRightShift-assign (>>>=) operation.
     *
     * @param line line in which the assignment operation occurs in the source file.
     * @param lhs  the lhs operand.
     * @param rhs  the rhs operand.
     */
    public JLRightShiftAssignOp(int line, JExpression lhs, JExpression rhs) {
        super(line, ">>>=", lhs, rhs);
    }

    int inst;
    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        if (!(lhs instanceof JLhs)) {
            JAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (JExpression) ((JLhs) lhs).analyzeLhs(context);
        }
        rhs = (JExpression) rhs.analyze(context);
        rhs.type().mustMatchExpected(line(), Type.INT);
        if (lhs.type().equals(Type.INT)) {
            type = Type.INT;
            inst = IUSHR;
        } else if (lhs.type().equals(Type.LONG)) {
            type = Type.LONG;
            inst = LUSHR;
        }
        else {
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for >>>=: " + lhs.type());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        ((JLhs) lhs).codegenLoadLhsRvalue(output);
        rhs.codegen(output);
        output.addNoArgInstruction(inst);
        ((JLhs) lhs).codegenDuplicateRvalue(output);
        ((JLhs) lhs).codegenStore(output);
    }
}

