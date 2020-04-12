package com.ellison.eigakensaku;

import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.http.HttpManager;
import com.ellison.eigakensaku.http.IMovieRequestCallback;

import org.junit.Assert;
import org.junit.Test;

public class HttpManagerTest {

    @Test
    public void testGetInstance() {
        HttpManager httpManager = HttpManager.getInstance();
        Assert.assertNotNull(httpManager);
    }

    @Test
    public void testRequestSearch() {
        HttpManager httpManager = HttpManager.getInstance();

        httpManager.requestMovieList("harry potter", Constants.OMDB_URL, new IMovieRequestCallback() {
            @Override
            public void onRequestSucceed(String keywords, MovieList list) {
                System.out.println("ellison list:" + list);
                Assert.assertNotNull(list);
            }

            @Override
            public void onRequestFailed(String keywords, String errorInfo) {
            }
        });


    }
}
