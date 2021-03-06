/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4j.netty.protocol.RouteRefreshPacket.java 
 */
package org.bgp4j.net.packets.refresh;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.OutboundRouteFilter;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.packets.BGPv4Packet;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RouteRefreshPacket extends BGPv4Packet {

	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private OutboundRouteFilter outboundRouteFilter; 
	
	public RouteRefreshPacket() {}
	
	public RouteRefreshPacket(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily) {
		setAddressFamily(addressFamily);
		setSubsequentAddressFamily(subsequentAddressFamily);
	}
	
	public RouteRefreshPacket(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, OutboundRouteFilter outboundRouteFilter) {
		this(addressFamily, subsequentAddressFamily);
		
		setOutboundRouteFilter(outboundRouteFilter);
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.BGPv4Packet#getType()
	 */
	@Override
	public int getType() {
		return BGPv4Constants.BGP_PACKET_TYPE_ROUTE_REFRESH;
	}

	/**
	 * @return the addressFamily
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}

	/**
	 * @param addressFamily the addressFamily to set
	 */
	public void setAddressFamily(AddressFamily addressFamily) {
		this.addressFamily = addressFamily;
	}

	/**
	 * @return the subsequentAddressFamily
	 */
	public SubsequentAddressFamily getSubsequentAddressFamily() {
		return subsequentAddressFamily;
	}

	/**
	 * @param subsequentAddressFamily the subsequentAddressFamily to set
	 */
	public void setSubsequentAddressFamily(
			SubsequentAddressFamily subsequentAddressFamily) {
		this.subsequentAddressFamily = subsequentAddressFamily;
	}

	/**
	 * @return the outboundRouteFilter
	 */
	public OutboundRouteFilter getOutboundRouteFilter() {
		return outboundRouteFilter;
	}

	/**
	 * @param outboundRouteFilter the outboundRouteFilter to set
	 */
	public void setOutboundRouteFilter(OutboundRouteFilter outboundRouteFilter) {
		this.outboundRouteFilter = outboundRouteFilter;
	}

	@Override
	public String toString() {
		return (new ToStringBuilder(this))
				.append("type", getType())
				.append("addressFamiliy", addressFamily)
				.append("outboundRouteFilter", outboundRouteFilter)
				.append("subsequentAddressFamily", subsequentAddressFamily)
				.toString();
	}
}
