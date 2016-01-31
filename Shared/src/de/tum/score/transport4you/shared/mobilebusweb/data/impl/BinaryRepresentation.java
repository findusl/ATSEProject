package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

/**
 * Classes implementing this interface provide the shortest possible binary representation
 * by returning a byte array that contains all necessary state and a header to 
 * @author istefo
 */
public interface BinaryRepresentation {
	public byte[] toBytes();
}
