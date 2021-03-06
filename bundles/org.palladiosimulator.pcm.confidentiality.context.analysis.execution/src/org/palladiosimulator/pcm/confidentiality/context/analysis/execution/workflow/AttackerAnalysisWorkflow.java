package org.palladiosimulator.pcm.confidentiality.context.analysis.execution.workflow;

import org.apache.log4j.Logger;
import org.palladiosimulator.pcm.confidentiality.context.analysis.execution.workflow.config.AttackerAnalysisWorkflowConfig;
import org.palladiosimulator.pcm.confidentiality.context.analysis.execution.workflow.job.AttackerAnalysisJob;
import org.palladiosimulator.pcm.confidentiality.context.analysis.execution.workflow.job.LoadContextJob;
import org.palladiosimulator.pcm.confidentiality.context.analysis.execution.workflow.job.LoadModifacationMarkJob;
import org.palladiosimulator.pcm.confidentiality.context.analysis.execution.workflow.job.LoadPCMAttack;
import static org.palladiosimulator.pcm.confidentiality.context.analysis.execution.partition.PartitionConstants.PARTITION_ID_MODIFICATION;

import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.SavePartitionToDiskJob;

/**
 * Workflow for AttackerAnalysis
 *
 * @author majuwa
 *
 *
 */
public class AttackerAnalysisWorkflow extends SequentialBlackboardInteractingJob<MDSDBlackboard> {
    private static final Logger LOGGER = Logger.getLogger(AttackerAnalysisWorkflow.class);

    public AttackerAnalysisWorkflow(final AttackerAnalysisWorkflowConfig config) {
        super(false);
        this.add(new LoadPCMAttack(config));
        this.add(new LoadContextJob(config));
        this.add(new LoadModifacationMarkJob(config));
        this.add(new AttackerAnalysisJob(config));
        this.add(new SavePartitionToDiskJob(PARTITION_ID_MODIFICATION));
    }
}
