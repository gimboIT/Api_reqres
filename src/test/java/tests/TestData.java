package tests;

import static utils.FakeUtils.*;

public class TestData {
    public static String
            fakeName = getFakeName(),
            fakeJob = getFakeJob(),
            fakeEmail = getFakeEmail(),
            fakePassword = getFakePassword();

    public static final Integer
            fakeId = getFakeUserId(),
            testUserId = 2;
}
