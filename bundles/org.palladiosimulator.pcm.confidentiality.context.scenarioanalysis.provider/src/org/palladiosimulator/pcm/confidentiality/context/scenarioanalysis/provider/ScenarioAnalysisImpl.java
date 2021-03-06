package org.palladiosimulator.pcm.confidentiality.context.scenarioanalysis.provider;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.osgi.service.component.annotations.Component;
import org.palladiosimulator.pcm.confidentiality.context.ConfidentialAccessSpecification;
import org.palladiosimulator.pcm.confidentiality.context.analysis.outputmodel.AnalysisResults;
import org.palladiosimulator.pcm.confidentiality.context.analysis.outputmodel.OutputmodelFactory;
import org.palladiosimulator.pcm.confidentiality.context.model.ContextAttribute;
import org.palladiosimulator.pcm.confidentiality.context.scenarioanalysis.api.PCMBlackBoard;
import org.palladiosimulator.pcm.confidentiality.context.scenarioanalysis.api.ScenarioAnalysis;
import org.palladiosimulator.pcm.confidentiality.context.scenarioanalysis.visitors.UsageModelVisitorScenarioRepository;
import org.palladiosimulator.pcm.confidentiality.context.set.ContextSet;
import org.palladiosimulator.pcm.confidentiality.context.specification.ContextSpecification;
import org.palladiosimulator.pcm.confidentiality.context.specification.PolicySpecification;
import org.palladiosimulator.pcm.confidentiality.context.specification.assembly.SystemPolicySpecification;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

@Component
public class ScenarioAnalysisImpl implements ScenarioAnalysis {

    @Override
    public AnalysisResults runScenarioAnalysis(PCMBlackBoard pcm, ConfidentialAccessSpecification context) {

        var usage = pcm.getUsageModel();
        if (context.getPcmspecificationcontainer().getPolicyspecification().stream()
                .anyMatch(SystemPolicySpecification.class::isInstance)) {
            return new ScenarioAnalysisSystemImpl().runScenarioAnalysis(pcm, context);
        }

        var result = OutputmodelFactory.eINSTANCE.createAnalysisResults();
        for (var scenario : usage.getUsageScenario_UsageModel()) {
            var visitor = new UsageModelVisitorScenarioRepository();
            var seffs = visitor.doSwitch(scenario.getScenarioBehaviour_UsageScenario());

            var output = OutputmodelFactory.eINSTANCE.createScenarioOutput();
            output.setResult(analysisScenario(scenario, seffs, context));
            output.setScenario(scenario);
            result.getScenariooutput().add(output);
        }

        return result;
    }

    private boolean analysisScenario(UsageScenario scenario, Set<ResourceDemandingBehaviour> behaviour,
            ConfidentialAccessSpecification context) {

        var contextSet = getContextSet(context.getPcmspecificationcontainer().getContextspecification(), scenario);
        var policyList = getContextSetsPolicy(context.getPcmspecificationcontainer().getPolicyspecification(),
                behaviour);

        for (var policySeff : policyList) {
            if (!checkContext(contextSet, policySeff))
                return false;
        }

        return true;
    }

    private boolean checkContext(ContextSet request, List<ContextSet> contextListPolicy) {
        if (contextListPolicy.isEmpty())
            return false;
        if (request.getContexts().isEmpty())
            return false;
        for (var policy : contextListPolicy) {
            if (checkContextSet(policy, request))
                return true;
        }
        return false;
    }

    private boolean checkContextSet(ContextSet policy, ContextSet request) {
        for (var policyItem : policy.getContexts()) {
            if (!checkContextAttribute(policyItem, request))
                return false;
        }
        return true;
    }

    private boolean checkContextAttribute(ContextAttribute policy, ContextSet request) {
        return request.getContexts().stream().anyMatch(e -> !policy.checkAccessRight(e));

    }

    private List<List<ContextSet>> getContextSetsPolicy(List<PolicySpecification> policySpecification,
            Set<ResourceDemandingBehaviour> behaviour) {
        return policySpecification.stream().filter(policy -> contains(policy, behaviour))
                .map(PolicySpecification::getPolicy).collect(Collectors.toList());
    }

    private boolean contains(PolicySpecification policy, Set<ResourceDemandingBehaviour> behaviours) {
        for (ResourceDemandingBehaviour behaviour : behaviours) {
            if (EcoreUtil.equals(policy.getResourcedemandingbehaviour(), behaviour))
                return true;
        }
        return false;
    }

    private ContextSet getContextSet(List<ContextSpecification> contextSpecification, UsageScenario scenario) {
        return contextSpecification.stream().filter(usage -> EcoreUtil.equals(scenario, usage.getUsagescenario()))
                .map(ContextSpecification::getContextset).findFirst().get();
    }
}
