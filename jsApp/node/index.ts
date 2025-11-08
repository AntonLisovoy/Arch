import {ru, WebModel} from "shared"
import * as readline from "node:readline";
import AppGesture = ru.otus.arch.data.AppGesture;
import AppUiState = ru.otus.arch.data.AppUiState;
import Error = ru.otus.arch.data.AppUiState.Error;
import UserList = ru.otus.arch.data.AppUiState.UserList;
import UserProfile = ru.otus.arch.data.AppUiState.UserProfile;

const model = new WebModel()

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

model.setStateObserver((state) => {
    render(state);
})

function parseCommand(command: string): AppGesture | undefined {
    const profileCommand = /^profile-(\d+)/i;

    if ("back" === command.toLocaleLowerCase()) {
        return AppGesture.Back;
    }
    if ("action" === command.toLocaleLowerCase()) {
        return AppGesture.Action;
    }
    const profileGesture = command.match(profileCommand);
    if (null !== profileGesture) {
        return new AppGesture.UserSelected(Number(profileGesture[1]));
    }
    return undefined;
}

function render(state: AppUiState) {
    switch (state.constructor.name) {
        case "Terminated":
            console.log("Terminated");
            rl.close();
            process.exit(0);
            return;
        case "Error":
            console.log((state as Error).error);
            console.log("Can retry: ", (state as Error).canRetry);
            return;
        case "UserList":
            console.log("Users:");
            (state as UserList).users.asJsReadonlyArrayView().forEach(user => {
                console.log(user.toString());
            })
            return;
        case "UserProfile":
            console.log("UserProfile:");
            console.log((state as UserProfile).profile.toString());
            return;
    }
    console.log(state);
}

function recursiveReadLine() {
    rl.question("Command: ", (answer) => {
        if ("exit" === answer.toLocaleLowerCase()) {
            return rl.close();
        }
        const command = parseCommand(answer);
        if (undefined !== command) {
            model.process(command)
        }
        recursiveReadLine();
    })
}

recursiveReadLine();