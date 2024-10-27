import {Meta, StoryObj} from "@storybook/react";
import DeleteActionDialog from "@/components/DeleteActionDialog";
import {action} from "@storybook/addon-actions";

const meta = {
    title: "Components/ActionDialog",
    component: DeleteActionDialog,
} satisfies Meta<typeof DeleteActionDialog>

export default meta;

type Story = StoryObj<typeof DeleteActionDialog>

export const Default: Story = {
    args: {
        dialogTitle: '댓글 삭제',
        openDialog: true,
        closeFn: () => {
            action("closeFn")("Closed")
        },
        deleteFn: () => {
            action("deleteFn")("Deleted")
        },
    },
};

