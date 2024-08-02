import { PropsWithChildren, ReactElement, ReactNode } from "react";

export default function BasicCard({ title, children }: BasicCardParams) {
    return (
        <div className="w-full p-6 bg-white border border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700">
            <h5 className="mb-2 text-md font-bold tracking-tight text-gray-900 dark:text-white">
                {title}
            </h5>
            <div className="mb-3 font-normal text-gray-700 dark:text-gray-400">
                {children}
            </div>
        </div>
    )
}

interface BasicCardParams {
    title: String,
    children?: ReactNode,
}