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
 * File: org.bgp4j.netty.protocol.BGPv4PacketEncodingTest.java 
 */
package org.bgp4j.netty.protocol;

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

import org.bgp4j.net.packets.BGPv4Packet;
import org.bgp4j.net.packets.KeepalivePacket;
import org.bgp4j.netty.BGPv4TestBase;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4PacketEncodingTest extends BGPv4TestBase {

	private BGPv4PacketEncoderFactory encoderFactory = new BGPv4PacketEncoderFactory();
	
	private ByteBuf encodePacket(BGPv4Packet packet) {
		ByteBuf buffer = allocator.buffer().order(ByteOrder.BIG_ENDIAN);
		
		encoderFactory.encoderForPacket(packet).encodePacket(packet, buffer);
		
		return buffer;
	}

	@Test
	public void encodeKeepalivePacket() {
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				0x00, 0x13,
				(byte)0x04, // type code KEEP				
		}, encodePacket(new KeepalivePacket()));
	}

}
