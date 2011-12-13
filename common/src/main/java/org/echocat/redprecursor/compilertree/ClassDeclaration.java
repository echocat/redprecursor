package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.*;

import javax.annotation.Nullable;
import java.util.List;

public interface ClassDeclaration extends Statement, Declaration, NameEnabledNode, ModifiersEnabledNode, DefinitionsEnabledNode, TypeParameterEnabledNode {

    public Identifier getExtends();

    public void setExtends(Identifier extendingType);

    public List<? extends Identifier> getImplements();

    public void setImplements(List<Identifier> implementingTypes);

    @Nullable
    public String getFullName();

}
