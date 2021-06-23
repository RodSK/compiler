package jminusminus;

import java.util.ArrayList;


/**
 * A representation of a interface declaration.
 */
class JInterfaceDeclaration extends JAST implements JTypeDecl {

    private ArrayList<String> mods;
    private String name;
    private ArrayList<JMember> interfaceBlock;
    private Type thisType;
    private ArrayList<Type> superType;
    private ClassContext context;

    /**
     * Constructs an AST node for a interface declaration.
     *
     * @param line    line in which the interface declaration occurs in the source file.
     *
     */
    public JInterfaceDeclaration(int line, ArrayList<String> mods, String name,
                                 ArrayList<Type> superType, ArrayList<JMember> interfaceBlock) {
        super(line);
        this.mods = mods;
        this.name = name;
        this.superType = superType;
        this.interfaceBlock = interfaceBlock;
    }

    /**
     * {@inheritDoc}
     */
    public void declareThisType(Context context) {

    }

    /**
     * {@inheritDoc}
     */
    public void preAnalyze(Context context) {

    }

    /**
     * {@inheritDoc}
     */
    public String name() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public Type thisType() {
        return thisType;
    }

    /**
     * {@inheritDoc}
     */
    public Type superType() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public JAST analyze(Context context) {
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
        json.addChild("JInterfaceDeclaration:" + line, e);
        if (mods != null) {
            ArrayList<String> value = new ArrayList<String>();
            for (String mod : mods) {
                value.add(String.format("\"%s\"", mod));
            }
            e.addAttribute("modifiers", value);
        }
        e.addAttribute("name", name);

//        if (context != null) {
//            context.toJSON(e);
//        }
        if (interfaceBlock != null) {
            for (JMember member : interfaceBlock) {
                ((JAST) member).toJSON(e);
            }
        }
    }

}

