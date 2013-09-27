/*******************************************************************************
 * Copyright (C) 2013  Stefan Schroeder
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * Contributors:
 *     Stefan Schroeder - initial API and implementation
 ******************************************************************************/
package basics.route;

import basics.route.VehicleTypeImpl.VehicleCostParams;

public class PenaltyVehicleType implements VehicleType{

	private VehicleType type;
	
	public PenaltyVehicleType(VehicleType type) {
		super();
		this.type = type;
	}

	@Override
	public String getTypeId() {
		return type.getTypeId();
	}

	@Override
	public int getCapacity() {
		return type.getCapacity();
	}

	@Override
	public VehicleCostParams getVehicleCostParams() {
		return type.getVehicleCostParams();
	}

	@Override
	public double getMaxVelocity() {
		return type.getMaxVelocity();
	}

	

}