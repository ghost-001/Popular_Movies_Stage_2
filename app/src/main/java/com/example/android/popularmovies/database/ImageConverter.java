package com.example.android.popularmovies.database;

import android.arch.persistence.room.TypeConverter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImageConverter {

    @TypeConverter
    public static byte[] FromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @TypeConverter
    public static Bitmap ToBitmap(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }
}
