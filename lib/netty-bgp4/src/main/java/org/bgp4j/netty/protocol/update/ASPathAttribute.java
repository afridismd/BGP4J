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

import org.bgp4j.net.PathSegmentType;
import org.bgp4j.netty.ASType;
import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ASPathAttribute extends Attribute implements ASTypeAware {

	public static class PathSegment {
		private ASType asType;
		private List<Integer> ases = new LinkedList<Integer>(); 
		private PathSegmentType pathSegmentType;
		
		public PathSegment(ASType asType) {
			this.asType = asType;
		}
		
		public PathSegment(ASType asType, PathSegmentType pathSegmentType, int[] asArray) {
			this(asType);

			this.pathSegmentType = pathSegmentType;
			
			for(int as : asArray)
				ases.add(as);
		}

		/**
		 * @return the asType
		 */
		public ASType getAsType() {
			return asType;
		}

		/**
		 * @return the ases
		 */
		public List<Integer> getAses() {
			return ases;
		}

		/**
		 * @param ases the ases to set
		 */
		public void setAses(List<Integer> ases) {
			this.ases = ases;
		}

		/**
		 * @return the type
		 */
		public PathSegmentType getPathSegmentType() {
			return pathSegmentType;
		}

		/**
		 * @param type the type to set
		 */
		public void setPathSegmentType(PathSegmentType type) {
			this.pathSegmentType = type;
		}

		private int getValueLength() {
			int size = 2; // type + length field

			if(this.ases != null && this.ases.size() > 0) {
				size += this.ases.size() * (asType == ASType.AS_NUMBER_4OCTETS ? 4 : 2);
			}
			
			return size;
		}

		private ChannelBuffer encodeValue() {
			ChannelBuffer buffer = ChannelBuffers.buffer(getValueLength());
			
			buffer.writeByte(PathSegmentTypeCodec.toCode(this.pathSegmentType));
			if(this.ases != null && this.ases.size() > 0) {
				buffer.writeByte(this.ases.size());
				
				for(int as : this.ases) {
					if(asType == ASType.AS_NUMBER_4OCTETS) 
						buffer.writeInt(as);
					else
						buffer.writeShort(as);
				}
					
				
			} else {
				buffer.writeByte(0);
			}
			return buffer;
		}
	}
	
	private ASType asType;
	private List<PathSegment> pathSegments = new LinkedList<PathSegment>(); 

	public ASPathAttribute(ASType asType) {
		super(Category.WELL_KNOWN_MANDATORY);
		
		this.asType = asType;
	}
	
	public ASPathAttribute(ASType asType, PathSegment[] segs) {
		this(asType);
		
		for(PathSegment seg : segs) {
			this.pathSegments.add(seg);
		}
	}

	@Override
	protected int getTypeCode() {
		return (isFourByteASNumber() 
				? BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH 
						: BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH);
	}

	@Override
	protected int getValueLength() {
		int size = 0; // type + length field

		if(this.pathSegments!= null) {
			for(PathSegment seg : this.pathSegments)
				size += seg.getValueLength();
		}
		
		return size;
	}

	@Override
	protected ChannelBuffer encodeValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(getValueLength());
		
		if(this.pathSegments != null && this.pathSegments.size() > 0) {
			for(PathSegment seg : this.pathSegments)
				buffer.writeBytes(seg.encodeValue());
		}
		
		return buffer;
	}

	/**
	 * @return the fourByteASNumber
	 */
	public boolean isFourByteASNumber() {
		return (this.asType == ASType.AS_NUMBER_4OCTETS);
	}

	/**
	 * @return the pathSegments
	 */
	public List<PathSegment> getPathSegments() {
		return pathSegments;
	}

	/**
	 * @param pathSegments the pathSegments to set
	 */
	public void setPathSegments(List<PathSegment> pathSegments) {
		this.pathSegments = pathSegments;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.ASTypeAware#getAsType()
	 */
	@Override
	public ASType getAsType() {
		return asType;
	}
}
