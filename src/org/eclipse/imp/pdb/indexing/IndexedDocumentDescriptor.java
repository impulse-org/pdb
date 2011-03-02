package org.eclipse.imp.pdb.indexing;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IDocument;

/**
 * This class describes an indexed document, encapsulating the resource to which it
 * corresponds, and the AST for the current document contents. Instances of this
 * class are typically sent by an Indexer to registered IFactUpdater implementations.
 * @see {@link IFactUpdater}, {@link Indexer}
 */
public class IndexedDocumentDescriptor {
    public final IDocument document;
    public final IResource resource;
    public final Object astRoot;

    public IndexedDocumentDescriptor(IDocument doc, IResource res, Object root) {
        this.document = doc;
        this.resource = res;
        this.astRoot = root;
    }
}