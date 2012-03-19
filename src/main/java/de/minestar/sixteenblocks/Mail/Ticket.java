/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of SixteenBlocks.
 * 
 * SixteenBlocks is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * SixteenBlocks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SixteenBlocks.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.sixteenblocks.Mail;

public class Ticket {

    private boolean isAnswered;
    private boolean isClosed;

    public Ticket(boolean isAnswered, boolean isClosed) {
        this.isAnswered = isAnswered;
        this.isClosed = isClosed;
    }

    public boolean isAnswered() {
        return isAnswered;
    }
    public boolean isClosed() {
        return isClosed;
    }

}
