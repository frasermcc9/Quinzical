// Copyright 2020 Fraser McCallum and Braden Palmer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//  
//     http://www.apache.org/licenses/LICENSE-2.0
//  
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package quinzical.impl.multiplayer.models.structures;

import quinzical.interfaces.multiplayer.XpClass;
import quinzical.interfaces.multiplayer.XpClassFactory;

import static quinzical.interfaces.multiplayer.XpClass.expFunction;
import static quinzical.interfaces.multiplayer.XpClass.inverseExpFunction;

public class XpClassFactoryImpl implements XpClassFactory {
    @Override
    public XpClass createXp(final int xp) {
        return new XpClassImpl(xp);
    }
}


class XpClassImpl implements XpClass {
    private final int xp;

    public XpClassImpl(final int xp) {
        this.xp = xp;
    }

    @Override
    public int getLevel() {
        return inverseExpFunction(xp);
    }

    @Override
    public int xpRemainingInLevel() {
        return expFunction(getLevel() + 1) - xp;
    }

    @Override
    public int xpDeltaBetweenLevels() {
        final int level = getLevel();
        final int next = expFunction(level + 1);
        final int current = expFunction(level);
        return next - current;
    }

    @Override
    public int xpThroughLevel() {
        return Math.max(0, this.xp - expFunction(getLevel()));
    }
}
