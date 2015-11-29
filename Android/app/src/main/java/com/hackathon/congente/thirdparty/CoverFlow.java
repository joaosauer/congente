package com.hackathon.congente.thirdparty;

/*
 * Copyright (C) 2010 Neil Davies
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
 *
 * This code is base on the Android Gallery widget and was Created
 * by Neil Davies neild001 'at' gmail dot com to be a Coverflow widget
 *
 * @author Neil Davies
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Gallery;

public class CoverFlow extends Gallery {

    public CoverFlow(Context context) {
        super(context);
        this.setStaticTransformationsEnabled(true);
    }

    public CoverFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStaticTransformationsEnabled(true);
    }

    public CoverFlow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStaticTransformationsEnabled(true);
    }
}
