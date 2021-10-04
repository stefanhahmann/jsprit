package com.graphhopper.jsprit.core.algorithm.termination;

import java.util.ArrayList;
import java.util.List;

import com.graphhopper.jsprit.core.algorithm.SearchStrategy;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Break;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;

public class NoUnassignedBreaksTermination implements PrematureAlgorithmTermination
{
    private final List<Break> requiredBreaks;

    public NoUnassignedBreaksTermination(VehicleRoutingProblem vrp)
    {
        requiredBreaks = new ArrayList<>();
        for (Vehicle vehicle : vrp.getVehicles())
        {
            if (vehicle.getBreak() == null)
                continue;
            requiredBreaks.add(vehicle.getBreak());
        }
    }

    @Override
    public boolean isPrematureBreak(SearchStrategy.DiscoveredSolution discoveredSolution)
    {
        List<Break> scheduledBreaks = new ArrayList<>();
        for (VehicleRoute route : discoveredSolution.getSolution().getRoutes())
        {
            for (TourActivity tourActivity : route.getActivities())
            {
                if (tourActivity instanceof Break)
                {
                    scheduledBreaks.add((Break) tourActivity);
                }
            }
        }
        if (scheduledBreaks.size() > 0)
        {
            System.out.println(scheduledBreaks.size());
        }
        boolean returnValue = scheduledBreaks.containsAll(requiredBreaks);
        return returnValue;
    }
}
