package ua.java.conferences.controller.actions;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ua.java.conferences.controller.actions.implementation.base.*;
import ua.java.conferences.controller.context.AppContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.java.conferences.controller.actions.ActionFactory.*;
import static ua.java.conferences.controller.actions.constants.ActionNames.SIGN_IN_ACTION;

class ActionFactoryTest {
    private final AppContext appContext = mock(AppContext.class);

    @Test
    void testCreateAction() {
        try (MockedStatic<AppContext> mocked = mockStatic(AppContext.class)) {
            mocked.when(AppContext::getAppContext).thenReturn(appContext);
            ActionFactory actionFactory = getActionFactory();
            Action action = actionFactory.createAction(SIGN_IN_ACTION);
            assertInstanceOf(SignInAction.class, action);
        }
    }

    @Test
    void testDefaultAction() {
        try (MockedStatic<AppContext> mocked = mockStatic(AppContext.class)) {
            mocked.when(AppContext::getAppContext).thenReturn(appContext);
            ActionFactory actionFactory = getActionFactory();
            Action action = actionFactory.createAction("wrongName");
            assertInstanceOf(DefaultAction.class, action);
        }
    }
}