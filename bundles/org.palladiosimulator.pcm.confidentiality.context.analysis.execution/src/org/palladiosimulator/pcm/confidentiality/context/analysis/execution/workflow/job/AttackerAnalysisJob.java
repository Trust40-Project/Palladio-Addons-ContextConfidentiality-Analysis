package org.palladiosimulator.pcm.confidentiality.context.analysis.execution.workflow.job;

import static org.palladiosimulator.pcm.confidentiality.context.analysis.execution.partition.PartitionConstants.PARTITION_ID_CONTEXT;
import static org.palladiosimulator.pcm.confidentiality.context.analysis.execution.partition.PartitionConstants.PARTITION_ID_PCM;

import org.eclipse.core.runtime.IProgressMonitor;
import org.palladiosimulator.analyzer.workflow.blackboard.PCMResourceSetPartition;
import org.palladiosimulator.pcm.confidentiality.context.analysis.execution.Activator;
import org.palladiosimulator.pcm.confidentiality.context.analysis.execution.partition.ContextPartition;
import org.palladiosimulator.pcm.confidentiality.context.analysis.execution.partition.ModificationMarkPartition;
import org.palladiosimulator.pcm.confidentiality.context.analysis.execution.partition.PartitionConstants;
import org.palladiosimulator.pcm.confidentiality.context.analysis.execution.workflow.config.ContextAnalysisWorkflowConfig;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import edu.kit.ipd.sdq.kamp4attack.core.AttackPropagationAnalysis;
import edu.kit.ipd.sdq.kamp4attack.core.BlackboardWrapper;

/**
 * Job specification to launch an attacker analysis. Before using the models should be loaded into
 * the corresponding MDSDBlackboard
 *
 * @author majuwa
 *
 */
public class AttackerAnalysisJob implements IBlackboardInteractingJob<MDSDBlackboard> {

    private MDSDBlackboard blackboard;

    public AttackerAnalysisJob(final ContextAnalysisWorkflowConfig config) {

    }

    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
//        final var analysis = Activator.getInstance().getAttackerAnalysis();

//        final var contextPartition = (ContextPartition) this.blackboard.getPartition(PARTITION_ID_CONTEXT);
//        final var pcmPartition = (PCMResourceSetPartition) this.blackboard.getPartition(PARTITION_ID_PCM);
//        final var dataPartition = (DataAttackPartition) this.blackboard.getPartition(PARTITION_ID_KASTEL);
//
//        // Fix someday
////        var attackerModel = dataPartition.getAdversaryModel();
////        var dataModel = dataPartition.getDataspecification();
//        analysis.runAttackerAnalysis(pcmPartition.getMiddlewareRepository(), contextPartition.getContextSpecification(),
//                null);
        var modificationPartition = ((ModificationMarkPartition) blackboard
                .getPartition(PartitionConstants.PARTITION_ID_MODIFICATION)).getModificationRepository();
        final var pcmPartition = (PCMResourceSetPartition) this.blackboard.getPartition(PARTITION_ID_PCM);
        var system =  pcmPartition.getSystem();
        var environment = pcmPartition.getResourceEnvironment();
        var allocation = pcmPartition.getAllocation();
        final var contextPartition = (ContextPartition) this.blackboard.getPartition(PARTITION_ID_CONTEXT);
        var specification =  contextPartition.getContextSpecification().getPcmspecificationcontainer();
        var wrapper = new BlackboardWrapper(modificationPartition, system, environment,allocation,specification);
        var propagation = new AttackPropagationAnalysis();
        propagation.runChangePropagationAnalysis(wrapper);
    }

    @Override
    public void cleanup(final IProgressMonitor monitor) throws CleanupFailedException {
        // TODO Provide clean up Operations

    }

    @Override
    public String getName() {
        return "AttackerAnalysis Job";
    }

    @Override
    public void setBlackboard(final MDSDBlackboard blackboard) {
        this.blackboard = blackboard;
    }
}
