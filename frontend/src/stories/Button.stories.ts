import { Meta, StoryObj } from "@storybook/react";
import Button from './../components/Button';
import { action } from "@storybook/addon-actions";

const meta = {
    title: "components/Button",
    component: Button,
} satisfies Meta<typeof Button>

export default meta;

type Story = StoryObj<typeof Button>

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