package com.graphhopper.jsprit.core.algorithm.listener;

import java.util.Collection;

import org.apache.commons.collections4.iterators.ReverseListIterator;

import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
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
                ReverseListIterator<TourActivity> reverseActivityIterator = new ReverseListIterator(
                    route.getActivities());

                // loop backwards through the list of tour activities
                while (reverseActivityIterator.hasNext())
                {
                    TourActivity activity = reverseActivityIterator.next();
                    TourActivity precedingActivity = activity.getPredecessor();
                    if (precedingActivity == null)
                        continue;
                    precedingActivity.getEndTime();
                    activity.getTransportTimeFromPredecessor();

                    // for activities with time windows: contract tour such that the vehicle leaves the preceding activity location only at the time, when it is needed to reach the current activity location at the beginning of the time window
                    if (activity.getTheoreticalEarliestOperationStartTime() != 0
                        && activity.getTheoreticalEarliestOperationStartTime() - activity
                            .getTransportTimeFromPredecessor() > precedingActivity.getEndTime())
                    {
                        precedingActivity
                            .setEndTime(activity.getTheoreticalEarliestOperationStartTime()
                                - activity.getTransportTimeFromPredecessor());
                        precedingActivity.setArrTime(
                            precedingActivity.getEndTime() - precedingActivity.getOperationTime());
                        activity.setArrTime(activity.getTheoreticalEarliestOperationStartTime());
                    }

                    // adjust the leaving time at the preceding activity such that the preceding activity location is only left at the time, when it is needed to reach the current activity location
                    else if (activity.getArrTime() - activity
                        .getTransportTimeFromPredecessor() > precedingActivity.getEndTime())
                    {
                        precedingActivity.setEndTime(
                            activity.getArrTime() - activity.getTransportTimeFromPredecessor());
                    }
                }
            }
        }
    }
}
