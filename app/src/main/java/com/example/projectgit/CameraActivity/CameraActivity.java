/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.projectgit.CameraActivity;

import android.app.Activity;

import android.content.Intent;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectgit.Frag.Camera2BasicFragment;
import com.example.projectgit.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


public class CameraActivity extends AppCompatActivity {
    private Camera2BasicFragment camera2BasicFragment;

    /**
     * This a callback object for the {@link ImageReader}. "onImageAvailable" will be called when a
     * still image is ready to be saved.
     */
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {

            Image mImage = reader.acquireNextImage();
            File mFile = new File(getExternalFilesDir(null), "profileImage.jpg");

            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("profilePath", mFile.toString());
                setResult(Activity.RESULT_OK, resultIntent);   //데이터를 보냄

                camera2BasicFragment.closeCamera();
                finish();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            camera2BasicFragment = new Camera2BasicFragment();
            camera2BasicFragment.setmOnImageAvailableListener(mOnImageAvailableListener);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, camera2BasicFragment)
                    .commit();
        }
    }

}
