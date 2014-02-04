/*
 * Copyright 2013 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.appengine.tck.blobstore;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.tck.blobstore.support.FileUploader;
import com.google.appengine.tck.lib.LibUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
public class GcsBlobstoreUploadTest extends BlobstoreTestBase {
    @Deployment
    public static WebArchive getDeployment() {
        WebArchive war = getBaseDeployment();

        LibUtils libUtils = new LibUtils();
        libUtils.addLibrary(war, "com.google.guava", "guava");
        libUtils.addLibrary(war, "com.google.appengine.tools", "appengine-gcs-client");

        return war;
    }

    @Test
    @RunAsClient
    @InSequence(10)
    public void testGcs(@ArquillianResource URL url) throws Exception {
        FileUploader fileUploader = new FileUploader();
        Map<String, String> params = new HashMap<>();
        params.put("bucket_name", "GcsBucket");
        params.put("successPath", "gcsHandler");
        String uploadUrl = fileUploader.getUploadUrl(new URL(url, "getUploadUrl"), params);
        String result = fileUploader.uploadFile(uploadUrl, "file", String.format("abc%s.txt", System.currentTimeMillis()), CONTENT_TYPE, "GcsTest".getBytes());
        Assert.assertEquals("GcsTest_123", result);
    }
}