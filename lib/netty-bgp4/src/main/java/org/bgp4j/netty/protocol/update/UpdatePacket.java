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
 */
package org.bgp4j.netty.protocol.update;

import java.util.LinkedList;
import java.util.List;

import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.NetworkLayerReachabilityInformation;
import org.bgp4j.netty.protocol.BGPv4Packet;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdatePacket extends BGPv4Packet {

	private List<NetworkLayerReachabilityInformation> withdrawnRoutes = new LinkedList<NetworkLayerReachabilityInformation>();
	private List<NetworkLayerReachabilityInformation> nlris = new LinkedList<NetworkLayerReachabilityInformation>();
	private List<Attribute> pathAttributes = new LinkedList<Attribute>();
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.BGPv4Packet#encodePayload()
	 */
	@Override
	protected ChannelBuffer encodePayload() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		ChannelBuffer withdrawnBuffer = encodeWithdrawnRoutes();
		ChannelBuffer pathAttributesBuffer = encodePathAttributes();
		
		buffer.writeShort(withdrawnBuffer.readableBytes());
		buffer.writeBytes(withdrawnBuffer);
		buffer.writeShort(pathAttributesBuffer.readableBytes());
		buffer.writeBytes(pathAttributesBuffer);
		buffer.writeBytes(encodeNlris());
		
		return buffer;
	}

	public int calculatePacketSize() {
		int size = BGPv4Constants.BGP_PACKET_MIN_SIZE_UPDATE;

		size += calculateSizeWithdrawnRoutes();
		size += calculateSizePathAttributes();
		size += calculateSizeNlris();
		
		return size;
	}
	
	private ChannelBuffer encodeWithdrawnRoutes() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);

		if(this.withdrawnRoutes != null) {
			for (NetworkLayerReachabilityInformation route : withdrawnRoutes) {
				buffer.writeBytes(route.encodeNLRI());
			}
		}
		
		return buffer;
	}

	private ChannelBuffer encodePathAttributes() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);

		if(this.pathAttributes != null) {
			for(Attribute pathAttribute : pathAttributes) {
				buffer.writeBytes(pathAttribute.encodePathAttribute());
			}
		}
		
		return buffer;
	}
	
	private ChannelBuffer encodeNlris() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);

		if(this.nlris != null) {
			for (NetworkLayerReachabilityInformation nlri : nlris) {
				buffer.writeBytes(nlri.encodeNLRI());
			}
		}
		
		return buffer;
	}

	private int calculateSizeWithdrawnRoutes() {
		int size = 0;

		if(this.withdrawnRoutes != null) {
			for (NetworkLayerReachabilityInformation route : withdrawnRoutes) {
				size += route.calculatePacketSize();
			}
		}

		return size;
	}
	
	private int calculateSizeNlris() {
		int size = 0;

		if(this.nlris != null) {
			for (NetworkLayerReachabilityInformation nlri : nlris) {
				size += nlri.calculatePacketSize();
			}
		}

		return size;
	}
	
	private int calculateSizePathAttributes() {
		int size = 0;
		
		if(this.pathAttributes != null) {
			for(Attribute  attr : pathAttributes)
				size += attr.calculateEncodedPathAttributeLength();
		}
		
		return size;
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.BGPv4Packet#getType()
	 */
	@Override
	protected int getType() {
		return BGPv4Constants.BGP_PACKET_TYPE_UPDATE;
	}

	/**
	 * @return the withdrawnRoutes
	 */
	public List<NetworkLayerReachabilityInformation> getWithdrawnRoutes() {
		return withdrawnRoutes;
	}

	/**
	 * @param withdrawnRoutes the withdrawnRoutes to set
	 */
	public void setWithdrawnRoutes(List<NetworkLayerReachabilityInformation> withdrawnRoutes) {
		this.withdrawnRoutes = withdrawnRoutes;
	}

	/**
	 * @return the nlris
	 */
	public List<NetworkLayerReachabilityInformation> getNlris() {
		return nlris;
	}

	/**
	 * @param nlris the nlris to set
	 */
	public void setNlris(List<NetworkLayerReachabilityInformation> nlris) {
		this.nlris = nlris;
	}

	/**
	 * @return the pathAttributes
	 */
	public List<Attribute> getPathAttributes() {
		return pathAttributes;
	}

	/**
	 * @param pathAttributes the pathAttributes to set
	 */
	public void setPathAttributes(List<Attribute> pathAttributes) {
		this.pathAttributes = pathAttributes;
	}

}
