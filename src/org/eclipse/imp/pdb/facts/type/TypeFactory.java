/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

package org.eclipse.imp.pdb.facts.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.eclipse.imp.pdb.facts.IValue;

public class TypeFactory {
    private static TypeFactory sInstance = new TypeFactory();

    private Map<Type,Type> fCache = new WeakHashMap<Type,Type>();

    private Map<String, NamedType> fNamedTypes= new HashMap<String, NamedType>();

    private ValueType sValueType= ValueType.getInstance();
    
    private ObjectType<Object> sProtoObjectType = new ObjectType<Object>(null);

    private IntegerType sIntegerType= IntegerType.getInstance();

    private DoubleType sDoubleType= DoubleType.getInstance();
    
    private NumberType sNumberType = NumberType.getInstance();

    private StringType sStringType= StringType.getInstance();

    private SourceRangeType sSourceRangeType= SourceRangeType.getInstance();

    private SourceLocationType sSourceLocationType= SourceLocationType.getInstance();

    private SetType sProtoSet = new SetType(null);

    private RelationType sProtoRelation= new RelationType((TupleType) null);

    private TuplePrototype sProtoTuple= TuplePrototype.getInstance();
    
    private NamedType sProtoNamedType = new NamedType(null, null);

    private ListType sProtoListType = new ListType(null);

    public static TypeFactory getInstance() {
        return sInstance;
    }

    private TypeFactory() { }

   

  
    public Type valueType() {
        return sValueType;
    }

    @SuppressWarnings("unchecked")
	public <T> ObjectType<T> objectType(Class<T> clazz) {
    	sProtoObjectType.fClass = clazz;
		Type result = fCache.get(sProtoObjectType);

		if (result == null) {
			result = new ObjectType(clazz);
			fCache.put(result, result);
		}
		return (ObjectType<T>) result;
    }
    
    public IntegerType integerType() {
        return sIntegerType;
    }

    public DoubleType doubleType() {
        return sDoubleType;
    }

    public NumberType numberType() {
    	return sNumberType;
    }
    
    public StringType stringType() {
        return sStringType;
    }

    public SourceRangeType sourceRangeType() {
        return sSourceRangeType;
    }

    public SourceLocationType sourceLocationType() {
        return sSourceLocationType;
    }

    private static class TuplePrototype extends TupleType {
        private static final TuplePrototype sInstance= new TuplePrototype();

        public static TuplePrototype getInstance() {
            return sInstance;
        }

        private int fAllocatedWidth;
        private int fWidth;
        private int fStart = 0;

        private TuplePrototype() {
            super(7, 0, new Type[7]);
        }

        /**
         * Sets the number of fields in this prototype tuple type. This forces the
         * recomputation of the (otherwise cached) hashcode.
         */
        private void setWidth(int N) {
            if ((fStart + N) > fAllocatedWidth) {
                Type[] newFieldTypes = new Type[fAllocatedWidth= (fStart + N)*2];
                System.arraycopy(fFieldTypes, 0, newFieldTypes, 0, fStart);
                fFieldTypes = newFieldTypes;
            }
            fWidth= N;
            fHashcode= -1;
        }

        /*package*/ Type[] getFieldTypes(int N) {
            setWidth(N);
            return fFieldTypes;
        }

        @Override
        public int hashCode() {
            if (fHashcode == -1) {
                fHashcode= 55501;
                for(int i= fStart, end = fStart + fWidth; i < end; i++) {
                    fHashcode= fHashcode * 44927 + fFieldTypes[i].hashCode();
                }
            }
            return fHashcode;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TupleType)) {
                return false;
            }
            TupleType other= (TupleType) obj;
            if (fWidth != other.fFieldTypes.length) {
                return false;
            }
            for(int i=fStart, j = 0, end = fStart + fWidth; i < end; i++, j++) {
                // N.B.: The field types must have been created and canonicalized before any
                // attempt to manipulate the outer type (i.e. SetType), so we can use object
                // identity here for the fFieldTypes.
                if (fFieldTypes[i] != other.fFieldTypes[j]) {
                    return false;
                }
            }
            return true;
        }
    }

   

    private TupleType getOrCreateTuple(int size, Type[] fieldTypes) {
    	Type result= fCache.get(sProtoTuple);

        if (result == null) {
            result= new TupleType(size, sProtoTuple.fStart, fieldTypes);
            fCache.put(result, result);
        }
        return (TupleType) result;
    }

    /*package */ TupleType tupleProduct(TupleType t1, TupleType t2) {
    	int N = t1.getArity() + t2.getArity();
    	Type[] fieldTypes = sProtoTuple.getFieldTypes(N);
    	
    	for(int i = 0; i < t1.getArity(); i++) {
    		fieldTypes[i] = t1.getFieldType(i);
    	}
    	for (int i = t1.getArity(), j = 0; i < N; i++, j++) {
    		fieldTypes[i] = t2.getFieldType(j);
    	}
    	return getOrCreateTuple(N, fieldTypes);
    }
    
    public TupleType tupleTypeOf(Type fieldType1) {
        Type[] fieldTypes= sProtoTuple.getFieldTypes(1);
        fieldTypes[0]= fieldType1;
        return getOrCreateTuple(1, fieldTypes);
    }

    public TupleType tupleTypeOf(Type fieldType1, Type fieldType2) {
        Type[] fieldTypes= sProtoTuple.getFieldTypes(2);
        fieldTypes[0]= fieldType1;
        fieldTypes[1]= fieldType2;
        return getOrCreateTuple(2, fieldTypes);
    }

    public TupleType tupleTypeOf(Type fieldType1, Type fieldType2, Type fieldType3) {
        Type[] fieldTypes= sProtoTuple.getFieldTypes(3);
        fieldTypes[0]= fieldType1;
        fieldTypes[1]= fieldType2;
        fieldTypes[2]= fieldType3;
        return getOrCreateTuple(3, fieldTypes);
    }

    public TupleType tupleTypeOf(Type fieldType1, Type fieldType2, Type fieldType3, Type fieldType4) {
        Type[] fieldTypes= sProtoTuple.getFieldTypes(4);
        fieldTypes[0]= fieldType1;
        fieldTypes[1]= fieldType2;
        fieldTypes[2]= fieldType3;
        fieldTypes[3]= fieldType4;
        return getOrCreateTuple(4, fieldTypes);
    }

    public TupleType tupleTypeOf(Type fieldType1, Type fieldType2, Type fieldType3, Type fieldType4, Type fieldType5) {
        Type[] fieldTypes= sProtoTuple.getFieldTypes(5);
        fieldTypes[0]= fieldType1;
        fieldTypes[1]= fieldType2;
        fieldTypes[2]= fieldType3;
        fieldTypes[3]= fieldType4;
        fieldTypes[4]= fieldType5;
        return getOrCreateTuple(5, fieldTypes);
    }

    public TupleType tupleTypeOf(Type fieldType1, Type fieldType2, Type fieldType3, Type fieldType4, Type fieldType5, Type fieldType6) {
        Type[] fieldTypes= sProtoTuple.getFieldTypes(6);
        fieldTypes[0]= fieldType1;
        fieldTypes[1]= fieldType2;
        fieldTypes[2]= fieldType3;
        fieldTypes[3]= fieldType4;
        fieldTypes[4]= fieldType5;
        fieldTypes[5]= fieldType6;
        return getOrCreateTuple(6, fieldTypes);
    }

    public TupleType tupleTypeOf(Type fieldType1, Type fieldType2, Type fieldType3, Type fieldType4, Type fieldType5, Type fieldType6, Type fieldType7) {
        Type[] fieldTypes= sProtoTuple.getFieldTypes(7);
        fieldTypes[0]= fieldType1;
        fieldTypes[1]= fieldType2;
        fieldTypes[2]= fieldType3;
        fieldTypes[3]= fieldType4;
        fieldTypes[4]= fieldType5;
        fieldTypes[5]= fieldType6;
        fieldTypes[6]= fieldType7;
        return getOrCreateTuple(7, fieldTypes);
    }

    public TupleType tupleTypeOf(List<Type> fieldTypes) {
        int N= fieldTypes.size();
        Type[] protoFieldTypes= sProtoTuple.getFieldTypes(N);
        for(int i=0; i < N; i++) {
            protoFieldTypes[i]= fieldTypes.get(i);
        }
        return getOrCreateTuple(N, protoFieldTypes);
    }
    
    public TupleType tupleTypeOf(IValue[] elements) {
        int N= elements.length;
        Type[] fieldTypes= sProtoTuple.getFieldTypes(N);
        for(int i=0; i < N; i++) {
            fieldTypes[i]= elements[i].getType();
        }
        return getOrCreateTuple(N, fieldTypes);
    }
    
    /**
     * Compute a new tupletype that is the lub of t1 and t2. 
     * Precondition: t1 and t2 have the same arity.
     * @param t1
     * @param t2
     * @return a TupleType which is the lub of t1 and t2
     */
    /* package */ TupleType lubTupleTypes(TupleType t1, TupleType t2) {
    	int N = t1.getArity();
    	
    	
    	// Note that this function may eventually be recursive (via lub) in the case of nested tuples.
    	// This poses a problem since we would overwrite sPrototuple.fFieldTypes
    	// Therefore fFieldTypes in the prototype is used as a stack, and fStart points to the
    	// bottom of the current stack frame.
    	// The goal is to prevent any kind of memory allocation when computing lub (for efficiency).
    	Type[] fieldTypes = sProtoTuple.getFieldTypes(N);
    	
    	// push the current frame to make room for the nested calls
    	int safeStart = sProtoTuple.fStart;
    	int safeWidth = sProtoTuple.fWidth;
    	sProtoTuple.fStart += N;
        
    	for (int i = safeStart, j = 0, end = safeStart + N; i < end; i++, j++) {
    		fieldTypes[i] = t1.getFieldType(j).lub(t2.getFieldType(j));
    	}
    	
    	// restore the current frame for creation of the tuple
    	sProtoTuple.fStart = safeStart;
    	sProtoTuple.fWidth = safeWidth;
    	sProtoTuple.fHashcode = -1;
    	TupleType result = getOrCreateTuple(N, fieldTypes);
    	
    	return result;
    }
    

    public SetType setTypeOf(Type eltType) {
        sProtoSet.fEltType= eltType;
        Type result= fCache.get(sProtoSet);

        if (result == null) {
            result= new SetType(eltType);
            fCache.put(result, result);
        }
        return (SetType) result;
    }

    /**
     * Constructs a new relation type from a tuple type. The tuple type
     * may be a named type (when type.getBaseType is a tuple type).
     * @param type a NamedType <= TupleType, or a TupleType
     * @return a relation type with the same field types that the tuple type has
     * @throws FactTypeError when type is not a tuple
     */
    public RelationType relType(Type type) throws FactTypeError {
    	if (type.isNamedType()) {
    		if (((NamedType) type).getBaseType().isTupleType()) {
    		  return relType((NamedType) type);
    		}
    	}
    	else if (type.isTupleType()) {
    		return relType((TupleType) type);
    	}
    	
    		
    	throw new FactTypeError("This is not a tuple type: " + type);
    }
    
    /**
     * Construct a new relation type from a named tuple type.
     * @param namedType the tuple type used to construct the field types
     * @return 
     * @throws FactTypeError
     */
    public RelationType relType(NamedType namedType) throws FactTypeError {
    	if (namedType.getBaseType().isTupleType()) {
    		 sProtoRelation.fTupleType= (TupleType) namedType.getBaseType();

    	        Type result= fCache.get(sProtoRelation);

    	        if (result == null) {
    	            result= new RelationType(namedType);
    	            fCache.put(result, result);
    	        }
    	        return (RelationType) result;
    	}
    	else {
    		throw new FactTypeError("Type " + namedType + " is not a tuple type");
    	}
    }
    
    /**
     * Construct a new relation type from a tuple type.
     * @param namedType the tuple type used to construct the field types
     * @return 
     * @throws FactTypeError
     */
    public RelationType relType(TupleType tupleType) {
        sProtoRelation.fTupleType= tupleType;

        Type result= fCache.get(sProtoRelation);

        if (result == null) {
            result= new RelationType(tupleType);
            fCache.put(result, result);
        }
        return (RelationType) result;
    }

    public RelationType relTypeOf(Type fieldType) {
        return relType(tupleTypeOf(fieldType));
    }

    public RelationType relTypeOf(Type fieldType1, Type fieldType2) {
        return relType(tupleTypeOf(fieldType1, fieldType2));
    }

    public RelationType relTypeOf(Type fieldType1, Type fieldType2, Type fieldType3) {
        return relType(tupleTypeOf(fieldType1, fieldType2, fieldType3));
    }

    public RelationType relTypeOf(Type fieldType1, Type fieldType2, Type fieldType3, Type fieldType4) {
        return relType(tupleTypeOf(fieldType1, fieldType2, fieldType3, fieldType4));
    }

    public RelationType relTypeOf(Type fieldType1, Type fieldType2, Type fieldType3, Type fieldType4, Type fieldType5) {
        return relType(tupleTypeOf(fieldType1, fieldType2, fieldType3, fieldType4, fieldType5));
    }

    public RelationType relTypeOf(Type fieldType1, Type fieldType2, Type fieldType3, Type fieldType4, Type fieldType5, Type fieldType6) {
        return relType(tupleTypeOf(fieldType1, fieldType2, fieldType3, fieldType4, fieldType5, fieldType6));
    }

    public RelationType relTypeOf(Type fieldType1, Type fieldType2, Type fieldType3, Type fieldType4, Type fieldType5, Type fieldType6, Type fieldType7) {
        return relType(tupleTypeOf(fieldType1, fieldType2, fieldType3, fieldType4, fieldType5, fieldType6, fieldType7));
    }
    
    public NamedType namedType(String name, Type superType) throws TypeDeclarationException {
    	sProtoNamedType.fName = name;
    	sProtoNamedType.fSuperType = superType;
    	
    	Type result= fCache.get(sProtoNamedType);

        if (result == null) {
        	NamedType old = fNamedTypes.get(name);
            if (old != null) {
             	throw new TypeDeclarationException("Can not redeclare type " + old + " with " + superType);
            }
        	 
            NamedType nt= new NamedType(name, superType);
            fCache.put(nt, nt);
            
           
            fNamedTypes.put(name, nt);
            result= nt;
        }
        return (NamedType) result;
    }

    public NamedType lookup(String name) {
        return fNamedTypes.get(name);
    }

    public ListType listType(Type type) {
		sProtoListType.fEltType = type;
		Type result= fCache.get(sProtoListType);

        if (result == null) {
            result= new ListType(type);
            fCache.put(result, result);
        }
        return (ListType) result;
	}

	/*package*/ TupleType tupleCompose(TupleType type, TupleType other) {
		int N = type.getArity() + other.getArity() - 2;
		Type[] fieldTypes = sProtoTuple.getFieldTypes(N);
		
		for (int i = 0; i < type.getArity() - 1; i++) {
			fieldTypes[i] = type.getFieldType(i);
		}
		
		for (int i = type.getArity() - 1, j = 1; i < N; i++, j++) {
			fieldTypes[i] = other.getFieldType(j);
		}
		
		return getOrCreateTuple(N, fieldTypes);
	}

	/*package*/ RelationType relationProduct(RelationType type1, RelationType type2) {
		int N = type1.getArity() + type2.getArity();
		Type[] fieldTypes = sProtoTuple.getFieldTypes(N);
		
		for (int i = 0; i < type1.getArity(); i++) {
			fieldTypes[i] = type1.getFieldType(i);
		}
		
		for (int i = type1.getArity(), j = 0; i < N; i++, j++) {
			fieldTypes[i] = type2.getFieldType(j);
		}
		
		return relType(getOrCreateTuple(N, fieldTypes));
	}
}