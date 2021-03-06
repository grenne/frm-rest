package com.khs.sherpa.json.service;

/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonJsonProvider implements JsonProvider {

	public String toJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T toObject(String json, Class<T> type) {
		// map to json object
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, type);
		}  catch (JsonMappingException e) {
			throw new RuntimeException("ERROR mapping JSON parameter - Exception:"+e);
		} catch (IOException e) {
			throw new RuntimeException("endpoint JSON parameter error - cannot deserialize JSON string "+e);
		}
	}

}
