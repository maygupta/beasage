package com.iskcon.isv.beasage;

/**
 * Created by sumitgarg on 27/03/17.
 */

public interface TrackedBookListener {
  void deleteRecord(final int id);
  void openBookDetails(int id,BookItem bookItem);

}
