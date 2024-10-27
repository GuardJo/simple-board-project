import {Meta, StoryObj} from "@storybook/react";
import TextareaActionDialog from "@/components/TextareaActionDialog";
import {action} from "@storybook/addon-actions";

const meta = {
    title: "Components/TextareaActionDialog",
    component: TextareaActionDialog,
} satisfies Meta<typeof TextareaActionDialog>

export default meta;

type Story = StoryObj<typeof TextareaActionDialog>;

export const Default: Story = {
    args: {
        dialogTitle: "댓글 수정",
        openDialog: true,
        content: "Old Content",
        formEventFn: (e: string) => {
            action("formEventFn")(`Update Content : ${e}`);
        },
        closeFn: () => {
            action("closeFn")("Closed")
        },
        submitFn: () => {
            action("submitFn")("Submitted")
        },
    },
};