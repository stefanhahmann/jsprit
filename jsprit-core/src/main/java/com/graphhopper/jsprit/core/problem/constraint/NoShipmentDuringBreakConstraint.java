package com.graphhopper.jsprit.core.problem.constraint;

import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.misc.JobInsertionContext;
import com.graphhopper.jsprit.core.problem.solution.route.activity.DeliverShipment;
import com.graphhopper.jsprit.core.problem.solution.route.activity.PickupShipment;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;

/**
 * TODO: this constraint currently blocks the whole break time window even in case where there is a
 * big time window, where a relatively short break could be taken. Thus, this constraint currently
 * may block valuable resources
 */
public class NoShipmentDuringBreakConstraint implements HardActivityConstraint
{

    public NoShipmentDuringBreakConstraint()
    {

    }

    @Override
    public ConstraintsStatus fulfilled(JobInsertionContext iFacts, TourActivity prevAct,
        TourActivity newAct, TourActivity nextAct, double prevActDepTime)
    {
        // ignore constraint, if vehicle does not even have a break
        if (iFacts.getNewVehicle().getBreak() == null)
            return ConstraintsStatus.FULFILLED;
        // ignore non-shipment activities
        if ( !(newAct instanceof PickupShipment || newAct instanceof DeliverShipment))
            return ConstraintsStatus.FULFILLED;
        // theoretical case that the job of a PickupShipment/DeliverShipment is not a Shipment
        Job job = ((TourActivity.JobActivity) newAct).getJob();
        if ( !(job instanceof Shipment))
            return ConstraintsStatus.FULFILLED;

        // init shipment
        Shipment shipment = (Shipment) job;

        double breakTimeWindowStart = iFacts.getNewVehicle().getBreak().getTimeWindow().getStart();
        double breakTimeWindowEnd = iFacts.getNewVehicle().getBreak().getTimeWindow().getEnd();
        double breakServiceDuration = iFacts.getNewVehicle().getBreak().getServiceDuration();

        double breakStart = breakTimeWindowStart;
        double breakEnd = breakTimeWindowStart + breakServiceDuration > breakTimeWindowEnd
            ? breakTimeWindowStart + breakServiceDuration
            : breakTimeWindowEnd;

        TimeWindow pickupTimeWindow = shipment.getPickupTimeWindow();
        if (isTimeWindowValid(pickupTimeWindow))
        {
            // if pickup starts during break
            if (pickupTimeWindow.getStart() > breakStart && pickupTimeWindow.getStart() < breakEnd)
                return ConstraintsStatus.NOT_FULFILLED;
            // if pick ends during break
            if (pickupTimeWindow.getEnd() > breakStart && pickupTimeWindow.getEnd() < breakEnd)
                return ConstraintsStatus.NOT_FULFILLED;
        }
        TimeWindow deliveryTimeWindow = shipment.getDeliveryTimeWindow();
        if (isTimeWindowValid(deliveryTimeWindow))
        {
            // if pickup starts during break
            if (deliveryTimeWindow.getStart() > breakStart
                && deliveryTimeWindow.getStart() < breakEnd)
                return ConstraintsStatus.NOT_FULFILLED;
            // if pick ends during break
            if (deliveryTimeWindow.getEnd() > breakStart && deliveryTimeWindow.getEnd() < breakEnd)
                return ConstraintsStatus.NOT_FULFILLED;
        }
        return ConstraintsStatus.FULFILLED;
    }

    private static boolean isTimeWindowValid(TimeWindow timeWindow)
    {
        return timeWindow != null
            && !(timeWindow.getStart() == 0 && timeWindow.getEnd() == Double.POSITIVE_INFINITY);
    }
}
