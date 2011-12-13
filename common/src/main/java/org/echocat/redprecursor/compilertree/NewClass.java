package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.*;

public interface NewClass extends CreateInstances, TypeEnabledNode, UsingTypeParameterEnabledNode, ArgumentsEnabledNode {

    public Expression getEnclosing();

    public void setEnclosing(Expression enclosing);

    public ClassDeclaration getClassDeclaration();

    public void setClassDeclaration(ClassDeclaration classDeclaration);

}
