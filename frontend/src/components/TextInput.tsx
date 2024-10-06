"use client"

import { ChangeEvent } from "react"

export default function TextInput({ labelName, placeholder, onChange = (v1:string) => {}, isSecret = false }: TextInputParams) {
    return (
        <div className="mb-6">
            <label htmlFor="default-input" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">{labelName}</label>
            <input type={isSecret ? "password" : "text"} id="default-input" onChange={(e) => onChange(e.target.value)} placeholder={placeholder} className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />
        </div>
    )
}

interface TextInputParams {
    labelName: string,
    placeholder: string,
    content?: string,
    onChange?: (e: string) => void,
    isSecret?: boolean,
};