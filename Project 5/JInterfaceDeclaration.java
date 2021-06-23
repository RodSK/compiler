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
    private ArrayList<Type> superTypes;
    private ClassContext context;


    /**
     * Constructs an AST node for a interface declaration.
     *
     * @param line    line in which the interface declaration occurs in the source file.
     *
     */
    public JInterfaceDeclaration(int line, ArrayList<String> mods, String name,
                                 ArrayList<Type> superTypes, ArrayList<JMember> interfaceBlock) {
        super(line);
        this.mods = mods;
        this.name = name;
        this.superTypes = superTypes;
        this.interfaceBlock = interfaceBlock;

        this.mods.add("abstract");
        this.mods.add("interface");
    }

    /**
     * {@inheritDoc}
     */
    public void declareThisType(Context context) {
        String qualifiedName = JAST.compilationUnit.packageName() == "" ?
                name : JAST.compilationUnit.packageName() + "/" + name;
        CLEmitter partial = new CLEmitter(false);
//        ArrayList<String> interfacez = new ArrayList<String>();
//        for (int i = 0; i < superTypes.size(); i++) {
//            interfacez.add(superTypes.get(i).jvmName());
//        }
        partial.addClass(mods, qualifiedName, Type.OBJECT.jvmName(), null, false);

        thisType = Type.typeFor(partial.toClass());
        context.addType(line, thisType);
    }

    /**
     * {@inheritDoc}
     */
    public void preAnalyze(Context context) {
        // Construct a class context.
        this.context = new ClassContext(this, context);

        // Resolve superclass.
        for (int i = 0; i < superTypes.size(); i++) {
            superTypes.set(i, superTypes.get(i).resolve(this.context));
            //thisType.checkAccess(line, superTypes.get(i));
            if (!superTypes.get(i).isInterface()) {
                JAST.compilationUnit.reportSemanticError(line, "Not Interface: %s",
                        superTypes.get(i).toString());
            }
        }

        // Create the (partial) class.
        CLEmitter partial = new CLEmitter(false);

        // Add the class header to the partial class
        String qualifiedName = JAST.compilationUnit.packageName() == "" ?
                name : JAST.compilationUnit.packageName() + "/" + name;

        ArrayList<String> interfacez = new ArrayList<String>();
        for (int i = 0; i < superTypes.size(); i++) {
            interfacez.add(superTypes.get(i).jvmName());
        }
        partial.addClass(mods, qualifiedName, Type.OBJECT.jvmName(), interfacez, false);

        for (JMember member : interfaceBlock) {
            if (member instanceof JMethodDeclaration) {
                JMethodDeclaration method = (JMethodDeclaration) member;
                method.setAbstract();
            }
        }
        // Pre-analyze the members and add them to the partial class.
        for (JMember member : interfaceBlock) {
            member.preAnalyze(this.context, partial);
        }

        // Get the ClassRep for the (partial) class and make it the representation for this type.
        Type id = this.context.lookupType(name);
        if (id != null && !JAST.compilationUnit.errorHasOccurred()) {
            id.setClassRep(partial.toClass());
        }

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
        return Type.VOID;
    }


    /**
     * {@inheritDoc}
     */
    public JAST analyze(Context context) {
        // Analyze all members
        for (JMember member : interfaceBlock) {
            ((JAST) member).analyze(this.context);
        }

        return this;
    }


    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {

        // The class header.
        String qualifiedName = JAST.compilationUnit.packageName() == "" ?
                name : JAST.compilationUnit.packageName() + "/" + name;

        ArrayList<String> interfacez = new ArrayList<String>();
        for (int i = 0; i < superTypes.size(); i++) {
            interfacez.add(superTypes.get(i).jvmName());
        }
        output.addClass(mods, qualifiedName, Type.OBJECT.jvmName(), interfacez, false);

        // The members.
        for (JMember member : interfaceBlock) {
            ((JAST) member).codegen(output);
        }


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

