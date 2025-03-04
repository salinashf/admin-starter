/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gm.mcayambe.core.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Player {
    
    private String name;
    
    private Map<Integer,Integer> goals;
    
    public Player() {
        goals = new LinkedHashMap<Integer,Integer>();
    }
    
    public Player(String name, Map<Integer,Integer> goals) {
        this.name = name;
        this.goals = goals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getGoals(int year) {
        return goals.get(year);
    }
}
