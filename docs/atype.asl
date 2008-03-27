// Creates a factory class xxxTypeFactory (e.g. InferenceTypeFactory)
// that canonicalizes the data type values, and the factory instance
// also serves as a locus for discarding groups of values at the end
// of an analysis. (The analysis results might hold onto some of the
// values, but at least the factory instance won't cause the values
// to live on in perpetuity.)

atype Term = QualifiedTypeName(String name)
          | Expression(Expr expr)
          | Method(ClassDecl owner, MethodDecl m)
          | Field(ClassDecl owner, FieldDecl f)
          | Param(MethodDecl owner, int idx)
          | Decl(MethodRef m)
          | Decl(FieldRef f)

type Type = String

atype TypeSet = set[Type]
             | Union(TypeSet lhs, TypeSet rhs)
             | Intersection(TypeSet lhs, TypeSet rhs)
             | Subtypes(TypeSet s)
             | Supertypes(TypeSet s)
             | Subtypes(Type t)
             | Supertypes(Type t)
             | EmptySet
             | Universe
             | SingletonType(Type t)
