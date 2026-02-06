import React, { forwardRef, useRef } from "react";

const DateInput = forwardRef(
  ({ register, name, disabled, ...props }, ref) => {
    const inputRef = useRef(null);

    const setRefs = (el) => {
      inputRef.current = el;
      if (typeof ref === "function") ref(el);
      else if (ref) ref.current = el;
    };

    const handleClick = () => {
      // MUST be inside user click
      if (inputRef.current?.showPicker) {
        inputRef.current.showPicker();
      }
    };

    return (
      <input
        {...register(name)}
        ref={setRefs}
        type="date"
        disabled={disabled}
        onClick={handleClick}   
        style={{
          width: "100%",
          cursor: disabled ? "not-allowed" : "pointer",
        }}
        {...props}
      />
    );
  }
);

export default DateInput;
