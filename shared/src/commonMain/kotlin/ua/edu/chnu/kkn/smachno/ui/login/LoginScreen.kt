package ua.edu.chnu.kkn.smachno.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import smachno.shared.generated.resources.Res
import smachno.shared.generated.resources.app_name
import smachno.shared.generated.resources.login_brand
import smachno.shared.generated.resources.login_demo_fill
import smachno.shared.generated.resources.login_demo_hint
import smachno.shared.generated.resources.login_hide
import smachno.shared.generated.resources.login_password
import smachno.shared.generated.resources.login_show
import smachno.shared.generated.resources.login_sign_in
import smachno.shared.generated.resources.login_subtitle
import smachno.shared.generated.resources.login_title
import smachno.shared.generated.resources.login_username
import ua.edu.chnu.kkn.smachno.ui.common.CenteredScrollColumn
import ua.edu.chnu.kkn.smachno.ui.common.ObserveEvents
import ua.edu.chnu.kkn.smachno.ui.common.isWideWindow

private const val BRAND_PANE_WEIGHT = 0.45f
private val FormMaxWidth = 460.dp

@Composable
internal fun LoginScreen(
    onSignedIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Помилки авторизації показуємо спливним повідомленням, а не в полі.
    ObserveEvents(viewModel.events) { event ->
        when (event) {
            LoginEvent.SignedIn -> onSignedIn()
            is LoginEvent.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        val content = Modifier.fillMaxSize().padding(innerPadding)
        // Стан один, розмітки дві: вибір залежить від ширини вікна.
        if (isWideWindow()) {
            LoginWide(state, viewModel::onAction, content)
        } else {
            LoginNarrow(state, viewModel::onAction, content)
        }
    }
}

@Composable
private fun LoginNarrow(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    CenteredScrollColumn(
        spacing = 24.dp,
        modifier = modifier.safeContentPadding(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Brand()
            Text(
                text = stringResource(Res.string.login_title),
                style = MaterialTheme.typography.displaySmall,
            )
            Text(
                text = stringResource(Res.string.login_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        LoginForm(state, onAction)
        SubmitButton(state, onAction, Modifier.fillMaxWidth())
        DemoHint(onAction)
    }
}

@Composable
private fun LoginWide(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.safeContentPadding()) {
        // Ліва колонка на широкому екрані - заставка з назвою застосунку.
        Column(
            modifier = Modifier
                .weight(BRAND_PANE_WEIGHT)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Brand(color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = stringResource(Res.string.login_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        Box(modifier = Modifier.weight(1f - BRAND_PANE_WEIGHT).fillMaxHeight()) {
            CenteredScrollColumn(
                spacing = 24.dp,
                modifier = Modifier.widthIn(max = FormMaxWidth),
                contentPadding = PaddingValues(40.dp),
            ) {
                Text(
                    text = stringResource(Res.string.login_title),
                    style = MaterialTheme.typography.displaySmall,
                )
                LoginForm(state, onAction)
                SubmitButton(state, onAction, Modifier.fillMaxWidth())
                DemoHint(onAction)
            }
        }
    }
}

/** Підказка з тестовим акаунтом: dummyjson інших не приймає. */
@Composable
private fun DemoHint(onAction: (LoginAction) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.login_demo_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        TextButton(onClick = { onAction(LoginAction.FillDemoCredentials) }) {
            Text(stringResource(Res.string.login_demo_fill))
        }
    }
}

@Composable
private fun LoginForm(state: LoginState, onAction: (LoginAction) -> Unit) {
    // Дія клавіатури в полі імені переводить фокус на пароль,
    // а в полі пароля одразу виконує вхід.
    val passwordFocus = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedTextField(
            value = state.username,
            onValueChange = { onAction(LoginAction.UsernameChanged(it)) },
            label = { Text(stringResource(Res.string.login_username)) },
            singleLine = true,
            enabled = !state.isLoading,
            isError = state.usernameError != null,
            supportingText = state.usernameError?.let { { Text(stringResource(it)) } },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { onAction(LoginAction.PasswordChanged(it)) },
            label = { Text(stringResource(Res.string.login_password)) },
            singleLine = true,
            enabled = !state.isLoading,
            isError = state.passwordError != null,
            supportingText = state.passwordError?.let { { Text(stringResource(it)) } },
            visualTransformation = if (state.isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                TextButton(onClick = { onAction(LoginAction.TogglePasswordVisibility) }) {
                    Text(
                        stringResource(
                            if (state.isPasswordVisible) Res.string.login_hide
                            else Res.string.login_show
                        )
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboard?.hide()
                    onAction(LoginAction.SignIn)
                },
            ),
            modifier = Modifier.fillMaxWidth().focusRequester(passwordFocus),
        )
    }
}

@Composable
private fun SubmitButton(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    Button(
        onClick = {
            keyboard?.hide()
            onAction(LoginAction.SignIn)
        },
        enabled = state.isSubmitEnabled,
        modifier = modifier,
        contentPadding = PaddingValues(14.dp),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            Text(stringResource(Res.string.login_sign_in))
        }
    }
}

@Composable
private fun Brand(color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary) {
    Text(
        text = stringResource(Res.string.login_brand),
        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
        color = color,
    )
}
