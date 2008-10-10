package org.eclipse.imp.pdb.facts;

public interface IValueVisitor {
   public IString visitString(IString o);
   public IDouble visitDouble(IDouble o);
   public IList visitList(IList o);
   public <T> IObject<T> visitObject(IObject<T> o);
   public IRelation visitRelation(IRelation o);
   public ISet visitSet(ISet o);
   public ISourceLocation visitSourceLocation(ISourceLocation o);
   public ISourceRange visitSourceRange(ISourceRange o);
   public ITuple visitTuple(ITuple o);
   public ITree visitTree(ITree o);
   public IInteger visitInteger(IInteger o);
   public IMap visitMap(IMap o);
}
