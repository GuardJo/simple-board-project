import {Meta, StoryObj} from "@storybook/react";
import {action} from "@storybook/addon-actions";
import BasicButton from "@/components/BasicButton";

const meta = {
    title: "components/BasicButton",
    component: BasicButton,
} satisfies Meta<typeof BasicButton>

export default meta;

type Story = StoryObj<typeof BasicButton>

export const noEvent: Story = {
    args: {
        children: "test",
    },
};


export const hasEvent: Story = {
    args: {
        children: "클릭 이벤트",
        onClick: () => {
            action("ClickEvent")("Clicked");
        }
    },
};