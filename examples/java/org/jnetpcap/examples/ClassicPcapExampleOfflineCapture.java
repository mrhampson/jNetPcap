/**
 * Copyright (C) 2010 Sly Technologies, Inc. This library is free software; you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version. This
 * library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jnetpcap.examples;

import java.util.Date;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

/**
 * This example is similar to the classic libpcap example shown in nearly every
 * tutorial on libpcap. The main difference is that a file is opened instead of
 * a live network interface. Using a packet handler it goes into a loop to read
 * a few packets, say 10. Prints some simple info about the packets, and then
 * closes the pcap handle and exits.
 * <p>
 * Here is the output generated by this example :
 * 
 * <pre>
 * Opening file for reading: tests/test-l2tp.pcap
 * Received at Tue Jan 27 16:17:17 EST 2004 caplen=114  len=114  jNetPcap rocks!
 * Received at Tue Jan 27 16:17:17 EST 2004 caplen=114  len=114  jNetPcap rocks!
 * Received at Tue Jan 27 16:17:18 EST 2004 caplen=114  len=114  jNetPcap rocks!
 * Received at Tue Jan 27 16:17:18 EST 2004 caplen=114  len=114  jNetPcap rocks!
 * Received at Tue Jan 27 16:17:19 EST 2004 caplen=114  len=114  jNetPcap rocks!
 * Received at Tue Jan 27 16:17:19 EST 2004 caplen=114  len=114  jNetPcap rocks!
 * Received at Tue Jan 27 16:17:20 EST 2004 caplen=114  len=114  jNetPcap rocks!
 * Received at Tue Jan 27 16:17:20 EST 2004 caplen=114  len=114  jNetPcap rocks!
 * Received at Tue Jan 27 16:17:21 EST 2004 caplen=114  len=114  jNetPcap rocks!
 * Received at Tue Jan 27 16:17:21 EST 2004 caplen=114  len=114  jNetPcap rocks!
 * </pre>
 * 
 * </p>
 * 
 * @author Mark Bednarczyk
 * @author Sly Technologies, Inc.
 */
public class ClassicPcapExampleOfflineCapture {

	/**
	 * Main startup method
	 * 
	 * @param args
	 *          ignored
	 */
	public static void main(String[] args) {
		/***************************************************************************
		 * First we setup error buffer and name for our file
		 **************************************************************************/
		final StringBuilder errbuf = new StringBuilder(); // For any error msgs
		final String file = "tests/test-l2tp.pcap";

		System.out.printf("Opening file for reading: %s%n", file);

		/***************************************************************************
		 * Second we open up the selected file using openOffline call
		 **************************************************************************/
		Pcap pcap = Pcap.openOffline(file, errbuf);

		if (pcap == null) {
			System.err.printf("Error while opening device for capture: "
			    + errbuf.toString());
			return;
		}

		/***************************************************************************
		 * Third we create a packet handler which will receive packets from the
		 * libpcap loop.
		 **************************************************************************/
		PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

			public void nextPacket(PcapPacket packet, String user) {

				System.out.printf("Received at %s caplen=%-4d len=%-4d %s\n", new Date(
				    packet.getCaptureHeader().timestampInMillis()), packet
				    .getCaptureHeader().caplen(), // Length actually captured
				    packet.getCaptureHeader().wirelen(), // Original length
				    user // User supplied object
				    );
			}
		};

		/***************************************************************************
		 * Fourth we enter the loop and tell it to capture 10 packets. The loop
		 * method does a mapping of pcap.datalink() DLT value to JProtocol ID, which
		 * is needed by JScanner. The scanner scans the packet buffer and decodes
		 * the headers. The mapping is done automatically, although a variation on
		 * the loop method exists that allows the programmer to sepecify exactly
		 * which protocol ID to use as the data link type for this pcap interface.
		 **************************************************************************/
		pcap.loop(10, jpacketHandler, "jNetPcap rocks!");

		/***************************************************************************
		 * Last thing to do is close the pcap handle
		 **************************************************************************/
		pcap.close();
	}
}
