package edu.kit.ipd.sdq.kamp4attack.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.jupiter.api.Test;

import edu.kit.ipd.sdq.kamp4attack.core.changepropagation.ContextChanges;
import edu.kit.ipd.sdq.kamp4attack.model.modificationmarks.KAMP4attackModificationmarks.CredentialChange;
import edu.kit.ipd.sdq.kamp4attack.model.modificationmarks.KAMP4attackModificationmarks.KAMP4attackModificationmarksFactory;

public class PropagationContextAssemblyTest extends AbstractChangeTests {
    
    @Test
    void testContextToAssemblyPropagationNoSpecificationNoContext() {
        final var change = KAMP4attackModificationmarksFactory.eINSTANCE.createCredentialChange();
        
        var compromissedComponent = this.createAssembly(change);
        var assemblyComponent = compromissedComponent.getAffectedElement();
        runContextToAssemblyPropagation(change);
        
        
        isNoContextChangeNoResourceNoLinking(change);
        
        assertEquals(1, change.getCompromisedassembly().size());
        assertTrue(EcoreUtil.equals(assemblyComponent, change.getCompromisedassembly().get(0).getAffectedElement()));
        assertFalse(change.isChanged());
        
    }
    @Test
    void testContextToAssemblyPropagationNoContextNoAssemblyComponent() {
        final var change = KAMP4attackModificationmarksFactory.eINSTANCE.createCredentialChange();
        
        
        runContextToAssemblyPropagation(change);                
        isNoContextChangeNoResourceNoLinking(change);
        
        assertTrue(change.getCompromisedassembly().isEmpty());
        assertFalse(change.isChanged());
        
    }
    @Test
    void testContextToAssemblyPropagationNoAssemblyComponent() {
        final var change = KAMP4attackModificationmarksFactory.eINSTANCE.createCredentialChange();
        
        
        var context = createContext("Test");
        var contextSet = createContextSet(context);
        
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(0));
        
        createPolicyResource(contextSet,this.environment.getResourceContainer_ResourceEnvironment().get(0));
        
        createPolicyLinking(contextSet, this.environment.getLinkingResources__ResourceEnvironment().get(0));
        
        
        runContextToAssemblyPropagation(change);                
        
        isNoContextChangeNoResourceNoLinking(change);
        assertTrue(change.getCompromisedassembly().isEmpty());
        assertFalse(change.isChanged());
        
    }
    @Test
    void testContextToAssemblyPropagationWrongContext() {
        final var change = KAMP4attackModificationmarksFactory.eINSTANCE.createCredentialChange();
        
        var ownedContext = createContext("Owned");
        var context = createContext("Test");
        var contextSet = createContextSet(context);
        
        var compromissedComponent = this.createAssembly(change);
        var assemblyComponent = compromissedComponent.getAffectedElement();
        
        createContextChange(ownedContext, change);
        
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(0));
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(1));
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(2));
        
        createPolicyResource(contextSet,this.environment.getResourceContainer_ResourceEnvironment().get(0));
        
        createPolicyLinking(contextSet, this.environment.getLinkingResources__ResourceEnvironment().get(0));
        
        
        runContextToAssemblyPropagation(change);                
        
        isNoResourceChangeLinkingChange(change);
        
        assertEquals(1, change.getContextchange().size());
        assertTrue(EcoreUtil.equals(ownedContext, change.getContextchange().get(0).getAffectedElement()));
        assertEquals(1, change.getCompromisedassembly().size());
        assertTrue(EcoreUtil.equals(assemblyComponent, change.getCompromisedassembly().get(0).getAffectedElement()));
        assertFalse(change.isChanged());
        
    }
    @Test
    void testContextToAssemblyContextNoSpecification() {
        final var change = KAMP4attackModificationmarksFactory.eINSTANCE.createCredentialChange();
        
        var ownedContext = createContext("Owned");
        var context = createContext("Test");
        var contextSet = createContextSet(context);
        
        var compromissedComponent = this.createAssembly(change);
        var assemblyComponent = compromissedComponent.getAffectedElement();
        
        createContextChange(ownedContext, change);
        
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(0));
        
        createPolicyResource(contextSet,this.environment.getResourceContainer_ResourceEnvironment().get(0));
        
        createPolicyLinking(contextSet, this.environment.getLinkingResources__ResourceEnvironment().get(0));
        
        
        runContextToAssemblyPropagation(change);                
        
        isNoResourceChangeLinkingChange(change);
        
        assertEquals(1, change.getContextchange().size());
        assertTrue(EcoreUtil.equals(ownedContext, change.getContextchange().get(0).getAffectedElement()));
        assertEquals(1, change.getCompromisedassembly().size());
        assertTrue(EcoreUtil.equals(assemblyComponent, change.getCompromisedassembly().get(0).getAffectedElement()));
        assertFalse(change.isChanged());
        
    }
    
    @Test
    void testContextToAssemblyPropagationProvided() {
        final var change = KAMP4attackModificationmarksFactory.eINSTANCE.createCredentialChange();
        
        var context = createContext("Test");
        var contextSet = createContextSet(context);
        
        var compromissedComponent = this.createAssembly(change);
        var assemblyComponent = compromissedComponent.getAffectedElement();
        
        createContextChange(context, change);
        
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(0));
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(1));
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(2));
        
        createPolicyResource(contextSet,this.environment.getResourceContainer_ResourceEnvironment().get(0));
        
        createPolicyLinking(contextSet, this.environment.getLinkingResources__ResourceEnvironment().get(0));
        
        
        runContextToAssemblyPropagation(change);                
        
        isNoResourceChangeLinkingChange(change);
        
        assertEquals(1, change.getContextchange().size());
        assertTrue(EcoreUtil.equals(context, change.getContextchange().get(0).getAffectedElement()));
        assertEquals(2, change.getCompromisedassembly().size());
        assertTrue(change.getCompromisedassembly().stream().anyMatch(e-> EcoreUtil.equals(e.getAffectedElement(), assembly.getAssemblyContexts__ComposedStructure().get(0))));
        assertTrue(change.getCompromisedassembly().stream().anyMatch(e-> EcoreUtil.equals(e.getAffectedElement(), assembly.getAssemblyContexts__ComposedStructure().get(2))));
        assertTrue(change.isChanged());
        
    }
    @Test
    void testContextToAssemblyPropagationRequiredNoSpecificationThirdComponent() {
        final var change = KAMP4attackModificationmarksFactory.eINSTANCE.createCredentialChange();
        
        var context = createContext("Test");
        var contextSet = createContextSet(context);        
        
        var compromissedComponent = this.createAssembly(change,assembly.getAssemblyContexts__ComposedStructure().get(2));
        var assemblyComponent = compromissedComponent.getAffectedElement();
        
        createContextChange(context, change);
        
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(0));
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(2));
        
        createPolicyResource(contextSet,this.environment.getResourceContainer_ResourceEnvironment().get(0));
        
        createPolicyLinking(contextSet, this.environment.getLinkingResources__ResourceEnvironment().get(0));
        
        
        runContextToAssemblyPropagation(change);                
        
        isNoResourceChangeLinkingChange(change);
        
        assertEquals(1, change.getContextchange().size());
        assertTrue(EcoreUtil.equals(context, change.getContextchange().get(0).getAffectedElement()));
        assertEquals(2, change.getCompromisedassembly().size());
        assertTrue(change.getCompromisedassembly().stream().anyMatch(e-> EcoreUtil.equals(e.getAffectedElement(), assembly.getAssemblyContexts__ComposedStructure().get(0))));
        assertTrue(change.getCompromisedassembly().stream().anyMatch(e-> EcoreUtil.equals(e.getAffectedElement(), assembly.getAssemblyContexts__ComposedStructure().get(2))));
        assertTrue(change.isChanged());
        
    }
    
    @Test
    void testContextToAssemblyPropagationRequiredSpecificationThirdComponentWrongContext() {
        final var change = KAMP4attackModificationmarksFactory.eINSTANCE.createCredentialChange();
        
        var context = createContext("Test");
        var contextSet = createContextSet(context);        
        
        var differentContext = createContext("different");
        var differentContextSet = createContextSet(differentContext);
        
        var compromissedComponent = this.createAssembly(change,assembly.getAssemblyContexts__ComposedStructure().get(2));
        var assemblyComponent = compromissedComponent.getAffectedElement();
        
        createContextChange(context, change);
        
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(0));
        createPolicyAssembly(differentContextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(0));
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(2));
        
        createPolicyResource(contextSet,this.environment.getResourceContainer_ResourceEnvironment().get(0));
        
        createPolicyLinking(contextSet, this.environment.getLinkingResources__ResourceEnvironment().get(0));
        
        
        runContextToAssemblyPropagation(change);                
        
        isNoResourceChangeLinkingChange(change);
        
        assertEquals(1, change.getContextchange().size());
        assertTrue(EcoreUtil.equals(context, change.getContextchange().get(0).getAffectedElement()));
        assertEquals(2, change.getCompromisedassembly().size());
        assertTrue(change.getCompromisedassembly().stream().anyMatch(e-> EcoreUtil.equals(e.getAffectedElement(), assembly.getAssemblyContexts__ComposedStructure().get(0))));
        assertTrue(change.getCompromisedassembly().stream().anyMatch(e-> EcoreUtil.equals(e.getAffectedElement(), assembly.getAssemblyContexts__ComposedStructure().get(2))));
        assertTrue(change.isChanged());
        
    }
    @Test
    void testContextToAssemblyPropagationRequired() {
        final var change = KAMP4attackModificationmarksFactory.eINSTANCE.createCredentialChange();
        
        var context = createContext("Test");
        var contextSet = createContextSet(context);        
        
        var compromissedComponent = this.createAssembly(change,assembly.getAssemblyContexts__ComposedStructure().get(2));
        var assemblyComponent = compromissedComponent.getAffectedElement();
        
        createContextChange(context, change);
        
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(0));
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(1));
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(2));
        
        createPolicyResource(contextSet,this.environment.getResourceContainer_ResourceEnvironment().get(0));
        
        createPolicyLinking(contextSet, this.environment.getLinkingResources__ResourceEnvironment().get(0));
        
        
        runContextToAssemblyPropagation(change);                
        
        isNoResourceChangeLinkingChange(change);
        
        assertEquals(1, change.getContextchange().size());
        assertTrue(EcoreUtil.equals(context, change.getContextchange().get(0).getAffectedElement()));
        assertEquals(3, change.getCompromisedassembly().size());
        assertTrue(change.getCompromisedassembly().stream().anyMatch(e-> EcoreUtil.equals(e.getAffectedElement(), assembly.getAssemblyContexts__ComposedStructure().get(0))));
        assertTrue(change.getCompromisedassembly().stream().anyMatch(e-> EcoreUtil.equals(e.getAffectedElement(), assembly.getAssemblyContexts__ComposedStructure().get(2))));
        assertTrue(change.getCompromisedassembly().stream().anyMatch(e-> EcoreUtil.equals(e.getAffectedElement(), assembly.getAssemblyContexts__ComposedStructure().get(1))));
        assertTrue(change.isChanged());
        
    }
    
    @Test
    void testContextToAssemblyPropagationDuplicate() {
        final var change = KAMP4attackModificationmarksFactory.eINSTANCE.createCredentialChange();
        
        var context = createContext("Test");
        var contextSet = createContextSet(context);        
        
        var compromissedComponent = this.createAssembly(change,assembly.getAssemblyContexts__ComposedStructure().get(0));
        var assemblyComponent = compromissedComponent.getAffectedElement();
        
        createContextChange(context, change);
        
        createPolicyAssembly(contextSet,this.assembly.getAssemblyContexts__ComposedStructure().get(0));
        
        createPolicyResource(contextSet,this.environment.getResourceContainer_ResourceEnvironment().get(0));
        
        createPolicyLinking(contextSet, this.environment.getLinkingResources__ResourceEnvironment().get(0));
        
        
        runContextToAssemblyPropagation(change);                
        
        isNoResourceChangeLinkingChange(change);
        
        assertEquals(1, change.getContextchange().size());
        assertTrue(EcoreUtil.equals(context, change.getContextchange().get(0).getAffectedElement()));
        assertEquals(1, change.getCompromisedassembly().size());
        assertTrue(change.getCompromisedassembly().stream().anyMatch(e-> EcoreUtil.equals(e.getAffectedElement(), assembly.getAssemblyContexts__ComposedStructure().get(0))));
        assertFalse(change.isChanged());
        
    }


    
    
    private void isNoContextChangeNoResourceNoLinking(final CredentialChange change) {
        assertTrue(change.getContextchange().isEmpty());
        isNoResourceChangeLinkingChange(change);
    }
    private void isNoResourceChangeLinkingChange(final CredentialChange change) {
        assertTrue(change.getCompromisedresource().isEmpty());
        assertTrue(change.getCompromisedlinkingresource().isEmpty());
    }
    
    private void runContextToAssemblyPropagation(CredentialChange change) {
        final var wrapper = this.getBlackboardWrapper();
        final var contextChange = new ContextChanges(wrapper);
        contextChange.calculateContextToAssemblyPropagation(change);
    }
}
