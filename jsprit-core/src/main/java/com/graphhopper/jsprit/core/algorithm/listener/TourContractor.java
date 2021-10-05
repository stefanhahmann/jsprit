package com.graphhopper.jsprit.core.algorithm.listener;

import java.util.Collection;

import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.Start;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;

public class TourContractor implements AlgorithmEndsListener
{
    @Override
    public void informAlgorithmEnds(VehicleRoutingProblem problem,
        Collection<VehicleRoutingProblemSolution> solutions)
    {
        for (VehicleRoutingProblemSolution solution : solutions)
        {
            for (VehicleRoute route : solution.getRoutes())
            {
                for (TourActivity activity : route.getActivities())
                {
                    TourActivity precedingActivity = activity.getPredecessor();
                    precedingActivity.getEndTime();
                    activity.getTransportTimeFromPredecessor();
                    // TODO maybe this even makes sense also for cases where the preceding activity is not of type Start
                    if (precedingActivity != null && precedingActivity instanceof Start
                        && activity.getTheoreticalEarliestOperationStartTime() != 0
                        && activity.getTheoreticalEarliestOperationStartTime() - activity
                            .getTransportTimeFromPredecessor() > precedingActivity.getEndTime())
                    {
                        precedingActivity
                            .setEndTime(activity.getTheoreticalEarliestOperationStartTime()
                                - activity.getTransportTimeFromPredecessor());
                        activity.setArrTime(activity.getTheoreticalEarliestOperationStartTime());
                    }
                }
            }
        }
    }
}
