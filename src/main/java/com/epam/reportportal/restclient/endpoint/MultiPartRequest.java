/*
 * Copyright 2016 EPAM Systems
 *
 *
 * This file is part of EPAM Report Portal.
 * https://github.com/reportportal/client-java-rest-core
 *
 * Report Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Report Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Report Portal.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epam.reportportal.restclient.endpoint;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;

/**
 * MultiPartRequest. Contains part which should be serialized and binary part to
 * be placed in Request
 * 
 * @author Andrei Varabyeu
 * 
 * @param <RQ>
 *            - Type of request to be serialized
 */
public class MultiPartRequest<RQ> {

	/** Set of Serialized Parts */
	private List<MultiPartSerialized<RQ>> serializedRQs;

	/** Set of binary parts */
	private List<MultiPartBinary> binaryRQs;

	public MultiPartRequest(List<MultiPartSerialized<RQ>> serializedRQs, List<MultiPartBinary> binaryRQs) {
		this.serializedRQs = serializedRQs;
		this.binaryRQs = binaryRQs;
	}

	public List<MultiPartBinary> getBinaryRQs() {
		return binaryRQs;
	}

	public List<MultiPartSerialized<RQ>> getSerializedRQs() {
		return serializedRQs;
	}

	/**
	 * Part of request to be serialized
	 * 
	 * @author Andrei Varabyeu
	 * 
	 * @param <RQ>
	 */
	public static class MultiPartSerialized<RQ> {

		private String partName;

		private RQ request;

		public MultiPartSerialized(String partName, RQ request) {
			this.partName = partName;
			this.request = request;
		}

		public String getPartName() {
			return partName;
		}

		public RQ getRequest() {
			return request;
		}
	}

	/**
	 * Binary part of request
	 * 
	 * @author Andrei Varabyeu
	 * 
	 */
	public static class MultiPartBinary {
		private String partName;
		private String filename;
		private String contentType;
		private ByteSource data;

		public MultiPartBinary(String partName, String filename, String contentType, ByteSource data) {
			this.partName = partName;
			this.filename = filename;
			this.data = data;
			this.contentType = contentType;
		}

		public ByteSource getData() {
			return data;
		}

		public String getFilename() {
			return filename;
		}

		public String getPartName() {
			return partName;
		}

		public String getContentType() {
			return contentType;
		}

	}

	/**
	 * Builder for multipart requests
	 * 
	 * @author Andrei Varabyeu
	 * 
	 * @param <RQ>
	 */
	public static class Builder<RQ> {
		private List<MultiPartSerialized<RQ>> serializedRQs;

		private List<MultiPartBinary> binaryRQs;

		public Builder() {
			serializedRQs = new ArrayList<MultiPartRequest.MultiPartSerialized<RQ>>();
			binaryRQs = new ArrayList<MultiPartRequest.MultiPartBinary>();
		}

		public Builder<RQ> addSerializedPart(String partName, RQ body) {
			serializedRQs.add(new MultiPartSerialized<RQ>(partName, body));
			return this;
		}

		public Builder<RQ> addBinaryPart(String partName, String filename, String contentType, @Nonnull ByteSource data) {
			Preconditions.checkNotNull(data, "Provided data shouldn't be null");
			binaryRQs.add(new MultiPartBinary(partName, filename, contentType, data));
			return this;
		}

		public MultiPartRequest<RQ> build() {
			return new MultiPartRequest<RQ>(serializedRQs, binaryRQs);
		}
	}
}
